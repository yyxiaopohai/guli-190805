package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.form.VideoInfoForm;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-02-12
 */
@Api("课时管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "更新课时")
    @PutMapping("update")
    public R updateCourseInfoById(
            @ApiParam(name = "video",value = "课时对象",required = true)
            @RequestBody VideoInfoForm videoInfoForm
    ){
        videoService.updateCourseInfoById(videoInfoForm);
        return R.ok().message("课时更新成功");
    }

    @ApiOperation(value = "根据id查询课时")
    @GetMapping("get/{id}")
    public R getVideInfoById(
            @ApiParam(name = "id",value = "课时id",required = true)
            @PathVariable String id
    ){
        VideoInfoForm videoInfoForm = videoService.getVideInfoById(id);
        return R.ok().data("item",videoInfoForm).message("查询课时成功");
    }

    @ApiOperation(value = "根据id删除课时")
    @DeleteMapping("delete/{id}")
    public R removeVideoById(
            @ApiParam(name = "id",value = "课时id",required = true)
            @PathVariable String id
    ){
        videoService.removeVideoById(id);
        return R.ok().message("课时删除成功");
    }

    @ApiOperation(value = "新增课时")
    @PostMapping("save")
    public R saveVideoInfo(
            @ApiParam(name = "videoInfoForm",value = "课时对象",required = true)
            @RequestBody VideoInfoForm videoInfoForm
    ){
        videoService.saveVideoInfo(videoInfoForm);
        return R.ok().message("课时保存成功");
    }

}

