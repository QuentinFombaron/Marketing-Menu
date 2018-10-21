package fombaron_valette.ihm.markingmenu;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

class MarkingMenuUI {
    private int nbItem;
    
    private Ellipse2D.Double circle;
    private List<Line2D.Double> lines = new ArrayList<>();
    private Arc2D.Double arc;

    private int menuX;
    private int menuY;


    MarkingMenuUI(double x, double y, double r, int nbItem) {
        this.nbItem = nbItem;
        this.circle = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
        this.drawLines(x, y, r);
        this.menuX = (int)x;
        this.menuY = (int)y;
    }
    
    /* Draw Arc Circle to show selected item */
    void drawSelectedItem(double menuX, double menuY, int i) {
        double angle = (double)-((360 / nbItem) * i);

        arc = new Arc2D.Double(
                menuX-50, 
                menuY-50, 
                100, 
                100,
                angle,
                (double)-(360/(nbItem)), Arc2D.PIE
        );
    }
    
    /* Draw lines in marking menu circle  */
    private void drawLines(double x, double y, double r) {
        for (int i = 0; i < nbItem; i++) {
            lines.add(new Line2D.Double(
                    x,
                    y,
                    (x + r * Math.cos((i * (2 * Math.PI / nbItem)))),
                    (y + r * Math.sin((i * (2 * Math.PI / nbItem))))
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
    
    int getMenuX() {
        return menuX;
    }

    int getMenuY() {
        return menuY;
    }

}
