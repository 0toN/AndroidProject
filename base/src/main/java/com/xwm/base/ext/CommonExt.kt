package com.xwm.base.ext

import java.lang.reflect.ParameterizedType


/**
 * 通过反射获取父类泛型 (T) 对应 Class类
 */
fun <T> getClazz(obj: Any): Class<T> {
    var type = (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    if (type is ParameterizedType) {
        type = type.rawType
    }
    return type as Class<T>
}




