package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author Helen
 * @since 2020-02-12
 */
public interface SubjectService extends IService<Subject> {

    void batchImport(InputStream inputStream) throws Exception;

    List<SubjectVO> nestedList();

    List<SubjectVO> nestedList2();
}
