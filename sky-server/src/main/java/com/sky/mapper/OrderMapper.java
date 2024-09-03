package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper
{
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);
}
