package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.edu.entity.vo.SubjectVO;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2020-02-12
 */
@CrossOrigin
@Api("课程类别信息")
@RestController
@RequestMapping("/admin/edu/subject")
@Slf4j
public class SubjectController {

    @Autowired
    private SubjectService subjectService;


    @ApiOperation(value = "excel导入课程类别信息")
    @PostMapping("import")
    public R batchImport(
            @ApiParam(name = "file",value = "excel文件",required = true)
            @RequestParam("file") MultipartFile file
    ){

        try {
            InputStream inputStream = file.getInputStream();
            subjectService.batchImport(inputStream);
            return R.ok().message("文件批量上传成功");
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.EXCEL_DATA_IMPORT_ERROR);
        }
    }

    @ApiOperation("嵌套数据")
    @GetMapping("nested-list")
    public R nestedList(){

        List<SubjectVO> subjectVOList = subjectService.nestedList();
        return R.ok().data("items",subjectVOList);

    }

    @ApiOperation("嵌套数据2")
    @GetMapping("nested-list2")
    public R nestedList2(){

        List<SubjectVO> subjectVOList = subjectService.nestedList2();
        return R.ok().data("items",subjectVOList);

    }
}

