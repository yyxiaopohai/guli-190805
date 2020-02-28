package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVO;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.MediaSize;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-02-12
 */
@Api("讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
@CrossOrigin
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "查询所有讲师集合",notes = "返回所有讲师的列表")
    @GetMapping("list")
    public R list(){

        List<Teacher> list = teacherService.list(null);
        return R.ok().data("items",list).message("获取讲师列表成功");
    }

    @ApiOperation(value = "根据id删除讲师信息",notes = "逻辑删除")
    @DeleteMapping("delete/{id}")
    public R delete(
            @ApiParam(name = "id",value = "讲师id",required = true)
            @PathVariable("id") String id){
//        int a = 1/0;
        boolean b = teacherService.removeById(id);
        return R.ok().message("删除讲师成功");
    }

    @ApiOperation(value = "根据id批量删除讲师信息")
    @DeleteMapping("batch-delete")
    public R deleteList(
            @ApiParam(name = "idList",value = "讲师id列表",required = true)
            @RequestBody List<String> idList){
//        int a = 1/0;
        teacherService.removeByIds(idList);
        return R.ok().message("批量删除讲师成功");
    }

    @ApiOperation(value = "分页查询讲师信息",notes = "查询讲师分页信息")
    @GetMapping("list/{page}/{limit}")
    public R index(
            @ApiParam(name = "page",value = "当前页",required = true)
            @PathVariable("page")Long page,
            @ApiParam(name = "limit",value = "每页的个数",required = true)
            @PathVariable("limit")Long limit,
            @ApiParam(name = "teacherQueryVO",value = "对象封装",required = false)
            TeacherQueryVO teacherQueryVO
    ){

        Page<Teacher> pageParam = new Page<>(page,limit);
        IPage<Teacher> teacherIPage = teacherService.selectPage(pageParam, teacherQueryVO);
        List<Teacher> records = teacherIPage.getRecords();
        long total = teacherIPage.getTotal();

        return R.ok().data("raws",records).data("total",total);
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("save")
    public R save(
            @ApiParam(name = "teacher",value = "讲师对象",required = true)
            @RequestBody Teacher teacher
    ){

        teacherService.save(teacher);
        return R.ok().message("新增讲师成功");
    }


    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("get/{id}")
    public R get(
            @ApiParam(name = "id",value = "讲师的编号",required = true)
            @PathVariable("id")Long id
    ){

        Teacher teacher = teacherService.getById(id);

        return R.ok().data("item",teacher).message("根据id查询成功");
    }

    @ApiOperation(value = "根据id更新讲师信息")
    @PutMapping("put")
    public R put(
            @ApiParam(name = "teacher",value = "讲师的信息",required = true)
            @RequestBody Teacher teacher
    ){
        teacherService.updateById(teacher);
        return R.ok().message("更新讲师成功");
    }

    @ApiOperation(value = "根据左侧关键字查询讲师名字",notes = "返回讲师的列表")
    @GetMapping("list/name/{key}")
    public R selectNameListByKey(
            @ApiParam(name = "key",value = "查询的关键子",required = true)
            @PathVariable("key")String key
    ){

        List<Map<String,Object>> nameList = teacherService.selectNameListByKey(key);
        return R.ok().data("nameList",nameList);
    }
}

