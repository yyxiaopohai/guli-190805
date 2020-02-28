package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.util.ExcelImportUtil;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectVO;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2020-02-12
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public void batchImport(InputStream inputStream) throws Exception {

        ExcelImportUtil excelImportUtil = new ExcelImportUtil(inputStream);

        HSSFSheet sheet = excelImportUtil.getSheet();

        for (Row rowData : sheet) {

            if (rowData.getRowNum() == 0) {
                continue;
            }

            //获取一级分类
            Cell levelOneCell = rowData.getCell(0);
            String levelOneCellValue = excelImportUtil.getCellValue(levelOneCell).trim();
            if (levelOneCell == null || StringUtils.isEmpty(levelOneCellValue)) {
                continue;
            }

            //获取二级分类
            Cell levelTwoCell = rowData.getCell(1);
            String levelTwoCellValue = excelImportUtil.getCellValue(levelTwoCell).trim();
            if (levelTwoCell == null || StringUtils.isEmpty(levelTwoCellValue)) {
                continue;
            }

            //先判断是否重复
            Subject subjectTitle = this.getByTitle(levelOneCellValue);
            String parentId = null;
            if (subjectTitle == null){

                //将一级分类存入数据库中，
                Subject subject = new Subject();
                subject.setTitle(levelOneCellValue);
                baseMapper.insert(subject);
                parentId = subject.getId();
            } else {
                parentId = subjectTitle.getId();
            }

            //判断二级分类
            Subject subjectTwoTitle = this.getSubByTitle(levelTwoCellValue, parentId);
            if (subjectTwoTitle == null) {
                Subject subject = new Subject();
                subject.setTitle(levelTwoCellValue);
                subject.setParentId(parentId);
                baseMapper.insert(subject);

            }

        }
    }


    private Subject getByTitle(String title){

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id",0);
        return baseMapper.selectOne(queryWrapper);

    }

    //二级分类
    private Subject getSubByTitle(String title,String parentId){

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id",parentId);
        return baseMapper.selectOne(queryWrapper);

    }

    @Override
    public List<SubjectVO> nestedList() {

        List<SubjectVO> subjectVOList = new ArrayList<>();

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort","id");
        List<Subject> subjectList = baseMapper.selectList(queryWrapper);

        List<Subject> subjectLevelOneList = new ArrayList<>();
        List<Subject> subjectLevelTwoList = new ArrayList<>();
        for (Subject subject : subjectList) {
            if (subject.getParentId().equals("0")) {
                subjectLevelOneList.add(subject);
            } else {
                subjectLevelTwoList.add(subject);
            }
        }

        //添加一级类别
        for (Subject subjectLevelOne : subjectLevelOneList) {
            SubjectVO subjectVOLevelOne = new SubjectVO();
            BeanUtils.copyProperties(subjectLevelOne,subjectVOLevelOne);
            subjectVOList.add(subjectVOLevelOne);

            List<SubjectVO> subjectVOLevelTwoList = new ArrayList<>();
            for (Subject subjectLevelTwo : subjectLevelTwoList) {
                if (subjectLevelOne.getId().equals(subjectLevelTwo.getParentId())){
                    SubjectVO subjectVoLevelTwo = new SubjectVO();
                    BeanUtils.copyProperties(subjectLevelTwo,subjectVoLevelTwo);
                    subjectVOLevelTwoList.add(subjectVoLevelTwo);
                }
            }
            subjectVOLevelOne.setChildren(subjectVOLevelTwoList);
        }
        return subjectVOList;
    }

    @Override
    public List<SubjectVO> nestedList2() {
        return baseMapper.selectNestedListByParentId("0");
    }

}
