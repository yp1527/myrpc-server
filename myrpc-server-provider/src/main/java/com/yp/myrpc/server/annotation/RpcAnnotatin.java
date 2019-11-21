package com.yp.myrpc.server.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 发布服务的自定义注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcAnnotatin {
    /**
     * 发布服务的类
     * @return
     */
    Class<?> value();

    /**
     * 版本号
     * @return
     */
    String version() default "";
}
