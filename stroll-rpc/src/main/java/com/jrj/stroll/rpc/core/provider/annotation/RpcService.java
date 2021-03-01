package com.jrj.stroll.rpc.core.provider.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * rpc service annotation, skeleton of stub ("@Inherited" allow service use "Transactional")
 * 
 * @author chenn
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcService {

	/**
     * @return
     */
    String version() default "";
}
