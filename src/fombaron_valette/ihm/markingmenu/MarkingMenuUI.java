package fombaron_valette.ihm.markingmenu;

import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

class MarkingMenuUI {
    private int nbItems;
    private Ellipse2D.Double circle;
    private List<Line2D.Double> lines = new ArrayList<>();

    private GeneralPath polygon;
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
        double angle1 = ((2 * Math.PI) / nbItems) * i;
        double angle2 = ((2 * Math.PI) / nbItems) * (i + 1.0);

        int xPoints[] = {
                (int) menuX,
                (int) (menuX + (50.0 * Math.cos(angle1))),
                (int) (menuX + (50.0 * Math.cos(angle2)))
        };
        int yPoints[] = {
                (int) menuY,
                (int) (menuY + (50.0 * Math.sin(angle1))),
                (int) (menuY + (50.0 * Math.sin(angle2)))
        };

        polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);
        polygon.moveTo(xPoints[0], yPoints[0]);

        for (int index = 1; index < xPoints.length; index++) {
            polygon.lineTo(xPoints[index], yPoints[index]);
        }

        polygon.closePath();
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

    GeneralPath getPolygon() {
        return polygon;
    }

    void setToNullPolygon() {
        this.polygon = null;
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
