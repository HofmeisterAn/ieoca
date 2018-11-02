package com.ieoca.components.algorithm.property;

import com.ieoca.components.utility.TypeConverter;
import javax.swing.*;

/**
 * @author Andre Hofmeister
 * @version 1.0
 */
public final class IJSlider extends IJProperty {
  public static final class Builder extends IJProperty.Builder<Builder> {
    Integer min;
    Integer max;
    Integer spaceing;

    public Builder(String name, String desc) {
      super(name, desc);
    }

    public Builder min(Integer min) {
      this.min = min;
      return this.getThis();
    }

    public Builder max(Integer max) {
      this.max = max;
      return this.getThis();
    }

    public Builder spaceing(Integer spaceing) {
      this.spaceing = spaceing;
      return this.getThis();
    }

    @Override
    public Builder getThis() {
      return this;
    }
  }

  private final Integer min;
  private final Integer max;
  private final Integer spaceing;

  public IJSlider(Builder builder) {
    super(builder);
    this.min = builder.min;
    this.max = builder.max;
    this.spaceing = builder.spaceing;
  }

  private Integer getMin() {
    return this.min;
  }

  private Integer getMax() {
    return this.max;
  }

  private Integer getSpaceing() {
    return this.spaceing;
  }

  @Override
  public Type getType() {
    return Type.SLIDER;
  }

  @Override
  public JComponent create(final JPanel jPanel) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> create(jPanel));
    }

    JLabel jLabel = new JLabel();
    jLabel.setText(this.getName());

    JSlider jSlider = new JSlider(this.getMin(), this.getMax());
    jSlider.setMajorTickSpacing(this.getSpaceing() * 2);
    jSlider.setMinorTickSpacing(this.getSpaceing());
    jSlider.setSnapToTicks(true);
    jSlider.setPaintTicks(true);
    jSlider.setPaintLabels(true);

    jPanel.add(jLabel);
    jPanel.add(jSlider);

    if (this.getValue() == null) return jSlider;

    Integer x = TypeConverter.toInteger(this.getValue());

    x = (((x != null) && (this.getMin() <= x) && (x <= this.getMax())) ? x : this.getMin());

    jSlider.setValue(x);

    return jSlider;
  }
}
