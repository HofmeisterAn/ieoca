package com.ieoca.components.algorithm.property.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Andre Hofmeister
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IComboBox {
  public String name() default "";

  public String desc() default "";

  public String[] elements() default "";
}
