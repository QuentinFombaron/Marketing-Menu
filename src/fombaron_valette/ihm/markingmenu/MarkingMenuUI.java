package fombaron_valette.ihm.markingmenu;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

class MarkingMenuUI {
    private int nbItems;
    private Ellipse2D.Double circle;
    private List<Line2D.Double> lines = new ArrayList<>();
    private List<Label> itemNames = new ArrayList<>();


    MarkingMenuUI(double x, double y, double r, int nbItems) {
        this.nbItems = nbItems;
        this.circle = new Ellipse2D.Double(x-r, y-r, 2*r, 2*r);
        this.drawLines(x, y, r);
        this.drawItemNames();
    }

    private void drawItemNames() {

    }

    private void drawLines(double x, double y, double r){
        for(int i = 0 ;i < nbItems; i++){
            lines.add(new Line2D.Double(
                    x,
                    y,
                    (x + r * Math.cos((i * (2*Math.PI/nbItems)))),
                    (y + r * Math.sin((i * (2*Math.PI/nbItems))))
            ));
        }
    }

    public List<Line2D.Double> getLines() {
        return lines;
    }

    public Ellipse2D.Double getCircle() {
        return circle;
    }
}
