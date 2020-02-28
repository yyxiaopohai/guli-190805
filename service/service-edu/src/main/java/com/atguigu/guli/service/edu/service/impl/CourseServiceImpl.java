package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVo;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-02-12
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;
    @Autowired
    private ChapterMapper chapterMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CourseCollectMapper courseCollectMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {

        //保存course表
        Course course = new Course();
        //设置课程的状态
        course.setStatus(Course.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.insert(course);
        //保存course描述信息表
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());

        courseDescriptionMapper.insert(courseDescription);
        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoFormById(String id) {

        //查询课程信息
        Course course = baseMapper.selectById(id);

        //查询课程描述信息
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);

        //封装到courseInfoForm中
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course,courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());
        return courseInfoForm;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {

        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.updateById(course);

        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(courseInfoForm.getId());
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescriptionMapper.updateById(courseDescription);
    }

    @Override
    public IPage<Course> selectPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");

        if (courseQueryVo == null) {
            return baseMapper.selectPage(pageParam,queryWrapper);
        }
        String teacherId = courseQueryVo.getTeacherId();
        String title = courseQueryVo.getTitle();
        String subjectId = courseQueryVo.getSubjectId();
        String subjectParentId = courseQueryVo.getSubjectParentId();

        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title",title);

        }

        if (!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("teacher_id",teacherId);
        }

        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("subject_id",subjectId);
        }

        return baseMapper.selectPage(pageParam,queryWrapper);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeCourseById(String id) {

        //删除和课程相关的所有信息

        //删除oss图片

        //删除vod视频

        //删除章节
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",id);
        chapterMapper.delete(chapterQueryWrapper);

        //删除评论
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id",id);
        commentMapper.delete(commentQueryWrapper);

        //删除收藏
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id",id);
        courseCollectMapper.delete(courseCollectQueryWrapper);

        //删除简介
        courseDescriptionMapper.deleteById(id);

        //删除课程视频
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",id);
        videoMapper.delete(videoQueryWrapper);

        //删除课程
        baseMapper.deleteById(id);

    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        return baseMapper.selectCoursePublishVoById(id);

    }

    @Override
    public void publishCourseById(String id) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        baseMapper.updateById(course);
    }
}