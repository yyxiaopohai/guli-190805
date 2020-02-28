package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVO;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-02-12
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {


    @Override
    public IPage<Teacher> selectPage(Page<Teacher> pageParam, TeacherQueryVO teacherQueryVO) {

        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Teacher> queryWrapper = teacherQueryWrapper.orderByAsc("sort");


        if (teacherQueryVO == null){
            return baseMapper.selectPage(pageParam,teacherQueryWrapper);
        }

        String name = teacherQueryVO.getName();
        Integer level = teacherQueryVO.getLevel();
        String begin = teacherQueryVO.getJoinDateBegin();
        String end = teacherQueryVO.getJoinDateEnd();

        if (!StringUtils.isEmpty(name)){
            queryWrapper.likeRight("name",name);
        }

        if (level != null) {
            queryWrapper.eq("level",level);
        }

        if (!StringUtils.isEmpty(begin)){
            queryWrapper.ge("join_date",begin);
        }

        if (!StringUtils.isEmpty(end)){
            queryWrapper.le("join_date",end);
        }
        return baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> selectNameListByKey(String key) {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        queryWrapper.likeRight("name",key);
        List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);
        return list;
    }
}
