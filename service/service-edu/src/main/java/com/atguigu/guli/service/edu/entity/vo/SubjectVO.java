package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectVO {

    private String id;
    private String title;
    private Integer sort;
    private List<SubjectVO> children = new ArrayList<>();
}
