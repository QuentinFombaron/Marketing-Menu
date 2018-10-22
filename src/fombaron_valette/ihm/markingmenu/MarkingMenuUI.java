package fombaron_valette.ihm.markingmenu;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

class MarkingMenuUI {
    private int nbItem;
    
    private Ellipse2D.Double circle;
    private List<Line2D.Double> lines = new ArrayList<>();
    private Arc2D.Double arcMenu;
    private Arc2D.Double arcSecondMenu;

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

        arcMenu = new Arc2D.Double(
                menuX-150,
                menuY-150,
                300,
                300,
                angle,
                (double)-(360/(nbItem)), Arc2D.PIE
        );
    }
    
    /* Draw lines in marking menu circle  */
    void drawLines(double menuX, double menuY, double r) {
        for (int i = 0; i < nbItem; i++) {
            lines.add(new Line2D.Double(
                    menuX,
                    menuY,
                    (menuX + r * Math.cos((i * (2 * Math.PI / nbItem)))),
                    (menuY + r * Math.sin((i * (2 * Math.PI / nbItem))))
            ));
        }
    }

    void drawSecondMenu(double menuX, double menuY, int i) {
        double angle = (double)-((360 / nbItem) * i);

        arcSecondMenu = new Arc2D.Double(
                menuX-250,
                menuY-250,
                500,
                500,
                angle,
                (double)-(360/(nbItem)), Arc2D.PIE
        );
    }

    void drawLinesSecondMenu () {

    }

    List<Line2D.Double> getLines() {
        return lines;
    }

    Ellipse2D.Double getCircle() {
        return circle;
    }

    Arc2D.Double getArcMenu() {
        return arcMenu;
    }

    Arc2D.Double getArcSecondMenu() {
        return arcSecondMenu;
    }

    void setToNullArc() {
        this.arcMenu = null;
        this.arcSecondMenu = null;
    }
    
    int getMenuX() {
        return menuX;
    }

    int getMenuY() {
        return menuY;
    }

}
