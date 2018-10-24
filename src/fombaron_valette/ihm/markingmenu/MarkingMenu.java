package fombaron_valette.ihm.markingmenu;

import java.util.LinkedList;
import java.util.List;

class MarkingMenu {

    private int nbItem;

    private List<String> itemNames;
    private List<String> secondItemToolNames;
    private List<String> secondItemColorNames;

    MarkingMenu(int nbItem, List<String> itemNames, List<String> secondItemToolNames, List<String> secondItemColorNames) {
        this.nbItem = nbItem;
        this.itemNames = itemNames;
        this.secondItemToolNames = secondItemToolNames;
        this.secondItemColorNames = secondItemColorNames;
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

    public String getItemNames(int index) {
        return this.itemNames.get(index);
    }

    public String getItemColorNames(int index) {
        return this.secondItemColorNames.get(index);
    }

    public String getItemToolNames(int index) {
        return this.secondItemToolNames.get(index);
    }
}
