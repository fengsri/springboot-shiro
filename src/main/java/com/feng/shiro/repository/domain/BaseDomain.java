package com.feng.shiro.repository.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 基本领域对象
 * @Author chenlinghong
 * @Date 2019/3/29 18:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDomain implements Serializable {

    private static final long serialVersionUID = 903519149398127132L;

    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间戳
     */
    private Date gmtCreate;

    /**
     * 最近修改时间戳
     */
    private Date gmtModified;

    /**
     * 是否删除
     */
    @JsonIgnore
    private Integer deleted;
}
