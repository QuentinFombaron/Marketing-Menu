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

    /* Get the selected item in the submenu */
    int getSelectedSecondItem(int mouseX, int mouseY, double menuX, double menuY, int nbItemSecondMenu) {
        double angle = Math.atan((mouseY - menuY) / (mouseX - menuX)) * (180 / Math.PI);

        if (mouseX > menuX && mouseY < menuY) {
            angle += 360;
        } else if (mouseX < menuX) {
            angle += 180;
        }

        int l = (int) (((angle * nbItem * nbItemSecondMenu) / 360) % nbItemSecondMenu);
        System.out.println("Second Item n° " + l);

        return l;
    }

    /* Return if mouse is in the principale marking menu or not */
    boolean inMenu(int mouseX, int mouseY, double menuX, double menuY) {
        double angle = Math.atan((mouseY - menuY) / (mouseX - menuX)) * (180 / Math.PI);
        if (Math.abs((mouseX-menuX) / Math.cos(angle / (180/Math.PI))) >= 150) {
            return false;
        }
        return true;
    }

    int getNbItems() {
        return nbItem;
    }
}
