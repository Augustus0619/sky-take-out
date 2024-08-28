package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品口味
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    //菜品id
    private Long dishId;

    //口味名称:甜度/温度/忌口/辣度
    private String name;

    //口味数据list 甜度：无糖，少糖，半糖，多糖，全糖/温度:热饮，常温，去冰，少冰，多冰
    private String value;

}
