package com.lokiy.cloud.common.base.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author Lokiy
 * @Date 2021/5/12 0012 14:17
 * @Description: 基本id对象
 */
@Data
@ApiModel
public class BaseIdEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("主键id")
    private Long id;
}
