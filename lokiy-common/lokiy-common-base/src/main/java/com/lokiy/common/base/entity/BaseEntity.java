package com.lokiy.common.base.entity;

import java.time.LocalDateTime;

/**
 * @author Lokiy
 * @date 2019/7/1 14:54
 * @description:
 */
public class BaseEntity {

    private Long id;

    private Integer delFlag;

    private String createUser;

    private LocalDateTime createTime;

    private String updateUser;

    private LocalDateTime updateTime;
}
