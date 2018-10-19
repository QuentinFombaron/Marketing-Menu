package fombaron_valette.ihm.markingmenu;

import java.awt.geom.*;

class MarkingMenuUI extends Ellipse2D.Double{
    private int nbItems;

    MarkingMenuUI(double x, double y, double w, double h, int nbItems) {
        super(x, y, w, h);
        this.nbItems = nbItems;
        this.drawItems(x, y, h);
    }

    private void drawItems(double x, double y, double h){
        for(int i = 0 ;i < nbItems; i++){
            new Line2D.Double(x,
                    y,
                    (x + (h) * Math.cos((i * (2*Math.PI/nbItems)))),
                    (y + (h) * Math.sin((i * (2*Math.PI/nbItems))))
            );
        }
    }
}
