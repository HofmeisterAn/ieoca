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
public @interface ISlider {
  public String name() default "";

  public String desc() default "";

  public int min() default 0;

  public int max() default 10;

  public int spaceing() default 1;
}
