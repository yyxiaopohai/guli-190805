package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.*;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.MediaSize;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-02-12
 */
@Api("课程管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private ChapterService chapterService;

    @ApiOperation(value = "新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfo(
            @ApiParam(name = "courseInfoForm", value = "课程基本信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm
    ){
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId",courseId);
    }

    @ApiOperation(value = "根据id查询课程")
    @GetMapping("course-info/{id}")
    public R getById(
            @ApiParam(name = "id", value = "课程id",required = true)
            @PathVariable String id
    ){
        CourseInfoForm courseInfoForm = courseService.getCourseInfoFormById(id);
        return R.ok().data("item",courseInfoForm);
    }


    @ApiOperation(value = "更新课程信息")
    @PutMapping("update-course-info")
    public R updateCourseInfoById(
            @ApiParam(name = "courseInfoForm", value = "课程信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm
    ){
        courseService.updateCourseInfoById(courseInfoForm);
        return R.ok().message("课程数据更新成功");
    }

    @ApiOperation(value = "分页查询课程信息",notes = "查询课程分页信息")
    @GetMapping("list/{page}/{limit}")
    public R index(
            @ApiParam(name = "page",value = "当前页",required = true)
            @PathVariable("page")Long page,
            @ApiParam(name = "limit",value = "每页的个数",required = true)
            @PathVariable("limit")Long limit,
            @ApiParam(name = "courseQueryVo",value = "对象封装",required = false)
                    CourseQueryVo courseQueryVo
    ){

        Page<Course> pageParam = new Page<>(page,limit);
        IPage<Course> courseIPage = courseService.selectPage(pageParam, courseQueryVo);
        List<Course> records = courseIPage.getRecords();
        long total = courseIPage.getTotal();

        return R.ok().data("rows",records).data("total",total);
    }

    @ApiOperation(value = "根据id删除课程")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(name = "courseQueryVo",value = "对象封装",required = false)
            @PathVariable String id
    ){

        courseService.removeCourseById(id);
        return R.ok().message("课程删除成功");
    }

    @ApiOperation(value = "根据ID获取课程发布信息")
    @GetMapping("course-publish-info/{id}")
    public R getCoursePublishVoById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){

        CoursePublishVo courseInfoForm = courseService.getCoursePublishVoById(id);
        return R.ok().data("item", courseInfoForm);
    }

    @ApiOperation(value = "根据id发布课程")
    @PutMapping("publish-course/{id}")
    public R publishCourseById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){

        courseService.publishCourseById(id);
        return R.ok();
    }



}

