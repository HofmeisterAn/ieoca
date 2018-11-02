package com.ieoca.components.algorithm.property;

import javax.swing.*;

/**
 * @author Andre Hofmeister
 * @version 1.0
 */
public abstract class IJProperty {
  public static enum Type {
    CHECKBOX,
    COMBOBOX,
    RADIOBUTTON,
    SLIDER,
    TEXTFIELD
  }

  public abstract static class Builder<T extends Builder<T>> {
    final String name;

    final String desc;

    Object value = null;

    public abstract T getThis();

    public Builder(String name, String desc) {
      this.name = name;
      this.desc = desc;
    }

    public final T value(Object object) {
      this.value = object;
      return this.getThis();
    }
  }

  private final String name;

  private final String desc;

  private final Object value;

  public abstract Type getType();

  public abstract JComponent create(final JPanel jPanel);

  IJProperty(Builder builder) {
    this.name = builder.name;
    this.desc = builder.desc;
    this.value = builder.value;
  }

  public final String getName() {
    return this.name;
  }

  public final String getDesc() {
    return this.desc;
  }

  final Object getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof IJProperty)) return false;

    IJProperty IJProperty = (IJProperty) o;

    if (!name.equals(IJProperty.name)) return false;

    if (!desc.equals(IJProperty.desc)) return false;

    return !(value != null ? !value.equals(IJProperty.value) : IJProperty.value != null);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + desc.hashCode();
    result = 31 * result + (value != null ? value.hashCode() : 0);
    result = 31 * result + this.getType().hashCode();
    return result;
  }
}
