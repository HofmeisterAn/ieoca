package com.ieoca.components.utility;

import java.lang.reflect.Constructor;
import java.math.BigInteger;

/**
 * A utility class to convert objects into different types. Some parts of this class are modified
 * from the Apache Commons Configuration 1.10 API. The original class PropertyConverter was
 * developed from Emmanuel Bourg.
 *
 * @author Andre Hofmeister
 * @version 1.0
 */
public final class TypeConverter {
  /** Constant for the prefix of hex numbers. */
  private static final String HEX_PREFIX = "0x";

  /** Constant for the radix of hex numbers. */
  private static final Integer HEX_RADIX = 16;

  /** Constant for the prefix of binary numbers. */
  private static final String BIN_PREFIX = "0b";

  /** Constant for the radix of binary numbers. */
  private static final Integer BIN_RADIX = 2;

  /** Constant for the argument classes of the Number construcotr that takes a String. */
  private static final Class<?>[] CONSTR_ARGS = {String.class};

  /**
   * Convert the specified object into a Boolean.
   *
   * @param value the value to convert.
   * @return the converted value.
   * @throws java.lang.UnsupportedOperationException
   */
  public static Boolean toBoolean(Object value) {
    Boolean b = null;

    if (value instanceof Boolean) {
      b = (Boolean) value;
    } else if (value instanceof Integer) {
      b = TypeConverter.toBoolean((Integer) value);
    } else if (value instanceof String) {
      b = TypeConverter.toBoolean((String) value);
    }

    if (b == null) {
      throw new UnsupportedOperationException();
    }

    return b;
  }

  /**
   * Convert the specified object into a Byte.
   *
   * @return the converted value.
   * @throws java.lang.UnsupportedOperationException
   */
  public static Byte toByte(Object value) {
    Number n = TypeConverter.toNumber(value, Byte.class);

    if (n instanceof Byte) return (Byte) n;

    return n.byteValue();
  }

  /**
   * Convert the specified object into a Short.
   *
   * @return the converted value.
   * @throws java.lang.UnsupportedOperationException
   */
  public static Short toShort(Object value) {
    Number n = TypeConverter.toNumber(value, Short.class);

    if (n instanceof Short) return (Short) n;

    return n.shortValue();
  }

  /**
   * Convert the specified object into a Integer.
   *
   * @return the converted value.
   * @throws java.lang.UnsupportedOperationException
   */
  public static Integer toInteger(Object value) {
    Number n = TypeConverter.toNumber(value, Integer.class);

    if (n instanceof Integer) return (Integer) n;

    return n.intValue();
  }

  /**
   * Convert the specified object into a Long.
   *
   * @return the converted value.
   * @throws java.lang.UnsupportedOperationException
   */
  public static Long toLong(Object value) {
    Number n = TypeConverter.toNumber(value, Long.class);

    if (n instanceof Long) return (Long) n;

    return n.longValue();
  }

  /**
   * Convert the specified object into a Float.
   *
   * @return the converted value.
   * @throws java.lang.UnsupportedOperationException
   */
  public static Float toFloat(Object value) {
    Number n = TypeConverter.toNumber(value, Float.class);

    if (n instanceof Float) return (Float) n;

    return n.floatValue();
  }

  /**
   * Convert the specified object into a Double.
   *
   * @return the converted value.
   * @throws java.lang.UnsupportedOperationException
   */
  public static Double toDouble(Object value) {
    Number n = TypeConverter.toNumber(value, Double.class);

    if (n instanceof Double) return (Double) n;

    return n.doubleValue();
  }

  private static Boolean toBoolean(Integer value) {
    if (value == null) return null;

    return (value.intValue() == 0) ? Boolean.FALSE : Boolean.TRUE;
  }

  private static Boolean toBoolean(String value) {
    if ("true".equalsIgnoreCase(value)) {
      return Boolean.TRUE;
    } else if ("false".equalsIgnoreCase(value)) {
      return Boolean.FALSE;
    } else if ("on".equalsIgnoreCase(value)) {
      return Boolean.TRUE;
    } else if ("off".equalsIgnoreCase(value)) {
      return Boolean.FALSE;
    } else if ("yes".equalsIgnoreCase(value)) {
      return Boolean.TRUE;
    } else if ("no".equalsIgnoreCase(value)) {
      return Boolean.FALSE;
    }

    return null;
  }

  private static Number toNumber(Object value, Class<?> targetClass) {
    if (value instanceof Number) return (Number) value;

    String str = value.toString();
    if (str.startsWith(HEX_PREFIX)) {
      try {
        return new BigInteger(str.substring(HEX_PREFIX.length()), HEX_RADIX);
      } catch (NumberFormatException nex) {
        throw new UnsupportedOperationException();
      }
    }

    if (str.startsWith(BIN_PREFIX)) {
      try {
        return new BigInteger(str.substring(BIN_PREFIX.length()), BIN_RADIX);
      } catch (NumberFormatException nex) {
        throw new UnsupportedOperationException();
      }
    }

    try {
      Constructor<?> constr = targetClass.getConstructor(CONSTR_ARGS);
      return (Number) constr.newInstance(new Object[] {str});
    } catch (Exception ex) {
      throw new UnsupportedOperationException();
    }
  }
}
