package com.xwm.base.ext

import java.lang.reflect.ParameterizedType


/**
 * 通过反射获取父类泛型 (T) 对应 Class类
 */
fun <T> getClazz(obj: Any): Class<T> {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
}




