package fombaron_valette.ihm.markingmenu;

import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

class MarkingMenuUI {
    private int nbItem;
    
    private Ellipse2D.Double circle;
    private List<Line2D.Double> lines = new ArrayList<>();

    private List<Line2D.Double> linesSecondMenu = new ArrayList<>();
    private Arc2D.Double arcMenu;
    private Arc2D.Double arcSecondMenu;
    private Arc2D.Double arcSelectedSecondMenu;

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

    /* Draw selected item in submenu */
    void drawSelectedSecondItem(double menuX, double menuY, int itemSelected, int itemSelectedSecondMenu, int nbItemSecondMenu) {
        double angle = -(((double)(360 / nbItem) * itemSelected) + ((itemSelectedSecondMenu+1)*((double)(360 / nbItem) / nbItemSecondMenu)));

        arcSelectedSecondMenu = new Arc2D.Double(
                menuX-250,
                menuY-250,
                500,
                500,
                angle,
                (double)((360 / nbItem) / nbItemSecondMenu), Arc2D.PIE
        );
    }
    
    /* Draw lines in marking menu circle  */
    private void drawLines(double menuX, double menuY, double r) {
        for (int i = 0; i < nbItem; i++) {
            lines.add(new Line2D.Double(
                    menuX,
                    menuY,
                    (menuX + r * Math.cos((i * (2 * Math.PI / nbItem)))),
                    (menuY + r * Math.sin((i * (2 * Math.PI / nbItem))))
            ));
        }
    }

    /* Draw submenu */
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

    /* Draw lines of submenu */
    void drawLinesSecondMenu (double menuX, double menuY, double r, int nbItemSecondMenu, int selectedItem) {
        double angle = (((2 * Math.PI) / nbItem) * selectedItem);

        for (int i = 0; i <= nbItemSecondMenu; i++) {
            linesSecondMenu.add(new Line2D.Double(
                    menuX,
                    menuY,
                    (menuX + r * Math.cos(angle + (i * (2 * Math.PI / nbItem)/nbItemSecondMenu))),
                    (menuY + r * Math.sin(angle + (i * (2 * Math.PI / nbItem)/nbItemSecondMenu)))
            ));
        }
    }

    List<Line2D.Double> getLines() {
        return lines;
    }

    List<Line2D.Double> getLinesSecondMenu() { return linesSecondMenu; }

    Ellipse2D.Double getCircle() {
        return circle;
    }

    Arc2D.Double getArcMenu() {
        return arcMenu;
    }

    Arc2D.Double getArcSecondMenu() {
        return arcSecondMenu;
    }

    Arc2D.Double getArcSelectedSecondMenu() { return arcSelectedSecondMenu; }

    void setToNullArc() {
        this.arcMenu = null;
        this.arcSecondMenu = null;
    }

    void resetLinesSecondMenu() {
        this.linesSecondMenu.clear();
    }
    
    int getMenuX() {
        return menuX;
    }

    int getMenuY() {
        return menuY;
    }

}
