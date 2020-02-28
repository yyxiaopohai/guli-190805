package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-02-12
 */
@Api("课程章节管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/edu/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @ApiOperation(value = "新增章节")
    @PostMapping("save")
    public R save(
            @ApiParam(name = "chapter",value = "章节对象",required = true)
            @RequestBody Chapter chapter
            ){
        chapterService.save(chapter);
        return R.ok().message("章节保存成功");
    }

    @ApiOperation(value = "根据id查询章节")
    @GetMapping("get/{id}")
    public R getById(
            @ApiParam(name = "id",value = "章节id",required = true)
            @PathVariable("id") String id
    ){
        Chapter chapter = chapterService.getById(id);
        return R.ok().data("item",chapter);
    }

    @ApiOperation(value = "更新章节")
    @PutMapping("update")
    public R update(
            @ApiParam(name = "chapter",value = "章节信息",required = true)
            @RequestBody Chapter chapter
    ){
        chapterService.updateById(chapter);
        return R.ok().message("章节更新成功");
    }


    @ApiOperation(value = "删除章节")
    @DeleteMapping("remove/{id}")
    public R removeChapterById(
            @ApiParam(name = "id",value = "章节id",required = true)
            @PathVariable("id") String id
    ){
        chapterService.removeChapterById(id);
        return R.ok();
    }

    @ApiOperation(value = "嵌套章节数据列表")
    @GetMapping("nested-list/{courseId}")
    public R nestedList(
            @ApiParam(name = "",value = "",required = true)
            @PathVariable("courseId")String courseId
    ){
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);
        return R.ok().data("items",chapterVoList);
    }
}

