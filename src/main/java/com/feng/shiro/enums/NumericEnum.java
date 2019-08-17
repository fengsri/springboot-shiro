package com.feng.shiro.enums;

import lombok.Getter;

/**
 * @Description 数字枚举
 * @Author chenlinghong
 * @Date 2019/4/19 0:46
 * @Version V1.0
 */
@Getter
public enum NumericEnum {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    ;

    /**
     * 具体数值
     */
    private long numeric;

    NumericEnum(long numeric){
        this.numeric = numeric;
    }
}
