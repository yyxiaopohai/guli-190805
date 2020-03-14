package com.atguigu.guli.service.order.service.impl;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.order.client.CourseClient;
import com.atguigu.guli.service.order.client.MemberClient;
import com.atguigu.guli.service.order.entity.Order;
import com.atguigu.guli.service.order.entity.PayLog;
import com.atguigu.guli.service.order.mapper.OrderMapper;
import com.atguigu.guli.service.order.mapper.PayLogMapper;
import com.atguigu.guli.service.order.service.OrderService;
import com.atguigu.guli.service.order.util.OrderNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author xiaozhang
 * @since 2020-03-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private MemberClient memberClient;

    @Autowired
    private PayLogMapper payLogMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveOrder(String courseId, String memberId) {

        //查询当前会员用户是否买过该课程
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        Order orderExist = baseMapper.selectOne(queryWrapper);
        //如果订单不为空，说明已经购买过当前课程
        if (orderExist != null){
            throw new GuliException(ResultCodeEnum.ORDER_EXIST_ERROR);
        }

        //如果为空,查询课程信息
        CourseDto coursedto = courseClient.getCourseDtoById(courseId);
        //课程信息为空，表示课程信息不存在
        if (coursedto ==null) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        //查询会员信息
        MemberDto memberdto = memberClient.getMemberDtoByMemberId(memberId);
        if (memberdto == null) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        //创建订单信息
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseCover(coursedto.getCover());
        order.setCourseTitle(coursedto.getTitle());
        order.setTeacherName(coursedto.getTeacherName());
        order.setTotalFee(coursedto.getPrice());
        order.setMemberId(memberdto.getId());
        order.setMobile(memberdto.getMobile());
        order.setNickname(memberdto.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public Boolean isBuyByCourseId(String memberId, String courseId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id",memberId).eq("course_id",courseId).eq("status",1);
        Integer count = baseMapper.selectCount(queryWrapper);

        return count.intValue() > 0;
    }

    @Override
    public Order getByOrderId(String orderId, String memberId) {

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId).eq("member_id",memberId);
        Order order = baseMapper.selectOne(queryWrapper);

        return order;
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     *	支付成功更改订单状态
     * @param map
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderStatus(Map<String, String> map) {

        //更新本地课程订单状态
        String orderNo = map.get("out_trade_no");
        Order order = this.getOrderByOrderNo(orderNo);
        order.setStatus(1);
        baseMapper.updateById(order);

        //记录支付日志
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderNo);
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee().longValue());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(new Gson().toJson(map));
        payLogMapper.insert(payLog);//插入到支付日志表

        //更改课程购买数量
        //TODO
    }
}
