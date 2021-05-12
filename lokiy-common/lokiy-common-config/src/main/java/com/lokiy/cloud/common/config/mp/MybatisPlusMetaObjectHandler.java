package com.lokiy.cloud.common.config.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lokiy.cloud.common.base.constant.BaseEntityFieldConsts;
import com.lokiy.cloud.common.base.constant.GlobalConstant;
import com.lokiy.cloud.common.util.generator.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Supplier;


/**
 * @author Lokiy
 * @date 2019/7/31 12:30
 * @description: mybatis-plus 自动填充
 */
@Component
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增时自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>start insert fill<<<<<<<<<<<<<<<<<<<<<<<<<");

        fillValue(metaObject, BaseEntityFieldConsts.ID, () -> SnowFlakeUtil.getFlowIdInstance().nextId());

        fillValue(metaObject, BaseEntityFieldConsts.DEL_FLAG, () -> GlobalConstant.DEFAULT_DEL_FLAG);

        fillValue(metaObject, BaseEntityFieldConsts.CREATE_USER, () -> GlobalConstant.DEFAULT_USER);

        fillValue(metaObject, BaseEntityFieldConsts.CREATE_TIME, () -> getDateValue( metaObject.getSetterType( BaseEntityFieldConsts.CREATE_TIME)));

        fillValue(metaObject, BaseEntityFieldConsts.UPDATE_USER, () -> GlobalConstant.DEFAULT_USER);

        fillValue(metaObject, BaseEntityFieldConsts.UPDATE_TIME, () -> getDateValue( metaObject.getSetterType( BaseEntityFieldConsts.UPDATE_TIME)));

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>end insert fill<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    /**
     * 更新时自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>start update fill<<<<<<<<<<<<<<<<<<<<<<<<<");

        fillValue(metaObject, BaseEntityFieldConsts.UPDATE_USER, () -> GlobalConstant.DEFAULT_USER);

        fillValue(metaObject, BaseEntityFieldConsts.UPDATE_TIME, () -> getDateValue( metaObject.getSetterType( BaseEntityFieldConsts.UPDATE_TIME)));

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>end update fill<<<<<<<<<<<<<<<<<<<<<<<<<");
    }


    /**
     * 填充字段
     * @param metaObject
     * @param fieldName
     * @param supplier
     */
    private void fillValue(MetaObject metaObject, String fieldName, Supplier<Object> supplier) {
        if (!metaObject.hasGetter(fieldName)) {
            return;
        }
        Object sidObj = metaObject.getValue(fieldName);
        if (sidObj == null && metaObject.hasSetter(fieldName) && supplier != null) {
            setFieldValByName(fieldName, supplier.get(), metaObject);
        }
    }


    /**
     * 获取时间
     * @param setterType
     * @return
     */
    private Object getDateValue(Class<?> setterType) {
        if (Date.class.equals(setterType)) {
            return new Date();
        } else if (LocalDateTime.class.equals(setterType)) {
            return LocalDateTime.now();
        }
        return null;
    }
}
