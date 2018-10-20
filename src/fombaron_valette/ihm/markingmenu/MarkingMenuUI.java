package fombaron_valette.ihm.markingmenu;

import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

class MarkingMenuUI {
    private int nbItems;
    private Ellipse2D.Double circle;
    private List<Line2D.Double> lines = new ArrayList<>();

    private Arc2D.Double arc;
    private List<JPanel> itemNames = new ArrayList<>();

    private int menuX;
    private int menuY;


    MarkingMenuUI(double x, double y, double r, int nbItems) {
        this.nbItems = nbItems;
        this.circle = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
        this.drawLines(x, y, r);
        this.drawItemNames();
        this.menuX = (int)x;
        this.menuY = (int)y;
    }

    private void drawItemNames() {
        for (int i = 0; i < nbItems; i++) {
            JPanel panel = new JPanel();
            panel.add(new JLabel("Item " + i));
            itemNames.add(panel);
        }
    }

    void drawSelectedItem(double menuX, double menuY, int i) {
        double angle = (double)-((360 / nbItems) * i);

        arc = new Arc2D.Double(menuX-50, menuY-50, 100, 100, angle, (double)-(360/(nbItems)), Arc2D.PIE);
    }

    private void drawLines(double x, double y, double r) {
        for (int i = 0; i < nbItems; i++) {
            lines.add(new Line2D.Double(
                    x,
                    y,
                    (x + r * Math.cos((i * (2 * Math.PI / nbItems)))),
                    (y + r * Math.sin((i * (2 * Math.PI / nbItems))))
            ));
        }
    }

    List<Line2D.Double> getLines() {
        return lines;
    }

    Ellipse2D.Double getCircle() {
        return circle;
    }

    Arc2D.Double getArc() {
        return arc;
    }

    void setToNullArc() {
        this.arc = null;
    }

    public List<JPanel> getItemNames() {
        return itemNames;
    }

    int getMenuX() {
        return menuX;
    }

    int getMenuY() {
        return menuY;
    }

}
