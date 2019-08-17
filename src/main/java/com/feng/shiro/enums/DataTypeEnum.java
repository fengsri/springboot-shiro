package com.feng.shiro.enums;

import lombok.Getter;

/**
 * @Description 数据类型
 * @Author chenlinghong
 * @Date 2019/6/18 20:53
 * @Version V1.0
 */
@Getter
public enum  DataTypeEnum {

    /**
     * 检验两种数据是否存在（动态数据，dxf数据）。动态数据dynamic表，dxf数据是（scene或者obstacle）
     */

    DYNAMIC(1,"动态数据"),
    DXF(2,"DXF数据"),

    ;

    /**
     * 数据类型编号
     */
    private Integer code;

    /**
     * 数据说明
     */
    private String message;

    DataTypeEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
