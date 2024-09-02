package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    //dishId和setmealId只能选一个（一次添加“+”,只可能添加一个菜品或是一个套餐，不可能两个都添加）
    private Long dishId;
    private Long setmealId;
    private String dishFlavor;

}
