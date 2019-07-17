package com.lokiy.cloud.common.base.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Lokiy
 * @date 2019/7/1 14:54
 * @description: 基础表对象
 */
@Data
public class BaseEntity extends Model{

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @TableField(fill = FieldFill.INSERT)
    private Long id;

    /**
     * 删除标志位
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
