package com.atguigu.guli.service.order.client;

import com.atguigu.guli.service.base.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("guli-edu")
public interface CourseClient {

    @GetMapping(value = "/api/edu/course/inner/get-course-dto/{courseId}")
    public CourseDto getCourseDtoById(@PathVariable("courseId") String courseId);
}
