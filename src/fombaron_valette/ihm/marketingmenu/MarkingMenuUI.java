package fombaron_valette.ihm.marketingmenu;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class MarkingMenuUI extends Ellipse2D.Double {
    private int nbItems;

    public MarkingMenuUI(double x, double y, double w, double h, int nbItems) {
        super(x, y, w, h);
        this.nbItems = nbItems;
        drawItems(x, y, w);
    }

    public void drawItems(double x, double y, double h){
        for(int i = 0 ; i < nbItems;i++){
            new Line2D.Double(x,
                    y,
                    (x + (h/2) * Math.cos((i * (360/nbItems) - (Math.PI / 2)))),
                    (y + (h/2) * Math.sin((i * (360/nbItems) - (Math.PI / 2))))
            );
        }
    }
}
