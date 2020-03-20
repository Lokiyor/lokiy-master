package com.lokiy.cloud.middleware.redis;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Lokiy
 * @date 2020/3/19 13:41
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("redis Hash结构 field过期基类对象")
public class RedisFieldExpireObject implements Serializable {

    @ApiModelProperty("过期时间点")
    private long expireTime;

    @ApiModelProperty("过期时间")
    private long ttl;

    @ApiModelProperty("存储的具体值")
    private Object value;
}
