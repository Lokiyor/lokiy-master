package com.lokiy.cloud.data.datasource.aspect;

import com.lokiy.cloud.data.datasource.DbContextHolder;
import com.lokiy.cloud.data.datasource.annotation.TargetDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Lokiy
 * @date 2019/7/19 16:48
 * @description:
 */
@Component
@Order(-10)
@Aspect
@Slf4j
public class DynamicDataSourceAspect {


    @Pointcut("@annotation(TargetDataSource)")
    public void dataSource(){

    }

    @Before("dataSource()")
    public void before(JoinPoint joinPoint) {
        log.info("-------------------------DynamicDataSourceAspect.before-------------------------");
        TargetDataSource targetDataSource = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(TargetDataSource.class);
        String dsId = targetDataSource.value();
        //如果不在数据源列表中,那么选择默认数据源
        if(!DbContextHolder.containsDataSource(dsId)) {
            log.warn(">>>>>>>>>>>>>>>>>>>>>>>>>数据源[{}]不存在,选择默认数据源 > {}", dsId, joinPoint.getSignature());
        } else {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>使用数据源: {} > {}", dsId, joinPoint.getSignature());
            //切换数据源上下文
            DbContextHolder.setDbType(dsId);

        }

    }

    @After("dataSource()")
    public void after(JoinPoint joinPoint) {
        log.info("-------------------------DynamicDataSourceAspect.after-------------------------");
        TargetDataSource targetDataSource = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(TargetDataSource.class);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>销毁当前数据源 {} > {}", targetDataSource.value(), joinPoint.getSignature());
        //方法执行完毕之后，销毁当前数据源信息
        DbContextHolder.clearDbType();
    }
}
