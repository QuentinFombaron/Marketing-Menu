package fombaron_valette.ihm.markingmenu;

class MarkingMenu {

    private int nbItem;

    MarkingMenu(int nbItem) {
        this.nbItem = nbItem;
    }

    /* Set selected item on the marking menu */
    int getSelectedItem(int mouseX, int mouseY, double menuX, double menuY) {
        double angle = Math.atan((mouseY - menuY) / (mouseX - menuX)) * (180 / Math.PI);

        if (mouseX > menuX && mouseY < menuY) {
            angle += 360;
        } else if (mouseX < menuX) {
            angle += 180;
        }

        int k = (int) ((angle * nbItem) / 360);
        System.out.println("Angle : " + (int) angle + "°; Item n° " + k);

        return k;
    }

    int getNbItems() {
        return nbItem;
    }
}
