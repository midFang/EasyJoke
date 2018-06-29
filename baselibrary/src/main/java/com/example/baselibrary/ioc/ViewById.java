package com.example.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fangsf on 2018/6/29.
 * Useful: 属性注入类
 */

@Target(ElementType.FIELD)  // 代表annotation的位置 FIELD属性, TYPE类上, method 方法上
@Retention(RetentionPolicy.RUNTIME)  // 代表什么时候生效, runtime运行时间生效, class, 编译时间生效
public @interface ViewById {
    int value();   //   @ViewById(R.id.button) 相当于里面的值
}
