package com.lokiy.cloud.common.config.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lokiy.cloud.common.base.constant.GlobalConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Lokiy
 * @date 2019/7/31 12:30
 * @description: mybatis-plus 自动填充
 */
@Component
@Slf4j
public class LokiyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增时自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>start insert fill<<<<<<<<<<<<<<<<<<<<<<<<<");
        LocalDateTime now = LocalDateTime.now();

        this.setInsertFieldValByName( GlobalConstant.BaseEntityField.DEL_FLAG, GlobalConstant.DefaultFlag.DEFAILT_DEL_FLAG, metaObject);
        this.setInsertFieldValByName( GlobalConstant.BaseEntityField.CREATE_TIME, now, metaObject);
        this.setInsertFieldValByName( GlobalConstant.BaseEntityField.UPDATE_TIME, now, metaObject);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>end insert fill<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    /**
     * 更新时自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>start update fill<<<<<<<<<<<<<<<<<<<<<<<<<");
        LocalDateTime now = LocalDateTime.now();

        this.setUpdateFieldValByName(GlobalConstant.BaseEntityField.UPDATE_TIME, now, metaObject);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>end update fill<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
}
