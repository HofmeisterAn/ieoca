package com.ieoca;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.Border;

public class ComponentTitleBorder implements MouseListener, SwingConstants, Border {
  private final Component component;

  private final JComponent container;

  private final Border border;

  private Rectangle rect;

  public ComponentTitleBorder(Component component, JComponent container, Border border) {
    this.component = component;
    this.container = container;
    this.border = border;
    this.container.addMouseListener(this);
  }

  public final boolean isBorderOpaque() {
    return true;
  }

  public final void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    CellRendererPane cellRendererPane = new CellRendererPane();

    Insets borderInsets = border.getBorderInsets(c);
    Insets insets = getBorderInsets(c);

    int temp = (insets.top - borderInsets.top) / 2;

    Dimension size = this.component.getPreferredSize();

    this.border.paintBorder(c, g, x, y + temp, width, height - temp);

    this.rect = new Rectangle(5, 0, size.width, size.height);

    cellRendererPane.paintComponent(g, this.component, (Container) c, this.rect);
  }

  public final Insets getBorderInsets(Component c) {
    Dimension size = this.component.getPreferredSize();

    Insets insets = this.border.getBorderInsets(c);
    insets.top = Math.max(insets.top, size.height);

    return insets;
  }

  @Override
  public final void mouseClicked(MouseEvent e) {
    this.dispatchEvent(e);
  }

  @Override
  public final void mousePressed(MouseEvent e) {
    this.dispatchEvent(e);
  }

  @Override
  public final void mouseReleased(MouseEvent e) {
    this.dispatchEvent(e);
  }

  @Override
  public final void mouseEntered(MouseEvent e) {
    this.dispatchEvent(e);
  }

  @Override
  public final void mouseExited(MouseEvent e) {
    this.dispatchEvent(e);
  }

  private void dispatchEvent(MouseEvent me) {
    if (this.rect != null && this.rect.contains(me.getX(), me.getY())) {
      Point pt = me.getPoint();
      pt.translate(-5, 0);

      this.component.setBounds(this.rect);
      this.component.dispatchEvent(
          new MouseEvent(
              this.component,
              me.getID(),
              me.getWhen(),
              me.getModifiers(),
              pt.x,
              pt.y,
              me.getClickCount(),
              me.isPopupTrigger(),
              me.getButton()));

      if (!this.component.isValid()) {
        this.container.repaint();
      }
    }
  }
}
