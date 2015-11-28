package view;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

class CustomPainter {

    protected static void paintComponentRounded(Component comp, Graphics g, int radius) {
	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	Component parent = comp.getParent();
	if (parent != null) { // fill in the corners where the rounded corners didn't paint with the parents color
	    int width = comp.getSize().width;
	    int height = comp.getSize().height;
	    Area area = new Area(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
	    Area borderArea = new Area(new Rectangle(0, 0, width, height));
	    borderArea.subtract(area);
	    g2d.setClip(borderArea);
	    g2d.setColor(parent.getBackground());
	    g2d.fillRect(0, 0, width, height);
	    g2d.setClip(null);
	}
    }

}