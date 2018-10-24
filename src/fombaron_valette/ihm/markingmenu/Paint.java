package fombaron_valette.ihm.markingmenu;

import static java.lang.Math.*;

import java.awt.geom.*;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.*;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.event.*;

class Paint extends JFrame {
    /* Hashmap containing the shape and the associated color */
    private HashMap<Shape, Color> shapes = new HashMap<>();
    private MarkingMenuUI menuUI;
    /* List containing the String in the menu dans the Color associated */
    private JComboBox<ComboItem> colorList = new JComboBox<>();
    /* Number of item in the menu */
    private int nbItem = 3;
    /* Number of item in the submenu */
    private int nbSecondMenu;

    /* Principal marking menu items */
    private List<String> itemNames = new LinkedList<>(Arrays.asList("Tools", "Colors", "Cancel"));

    /* Submenu Tool items names */
    private List<String> secondItemToolNames = new LinkedList<>(Arrays.asList("Pen", "Rect.", "Elips."));

    /* Submenu Color items names */
    private List<String> secondItemColorNames = new LinkedList<>(Arrays.asList("Black", "Red", "Orange", "Yellow", "Green", "Indigo", "Blue", "Purple"));


    private MarkingMenu markingMenu = new MarkingMenu(nbItem, itemNames, secondItemToolNames, secondItemColorNames);
    private boolean inMenu = true;

    private int selectedItem;
    private int selectedSecondItem = -1;

    /* Boolean to correct right + left mouse click in the same time bug */
    private boolean rightButtonPressed;

    private Graphics2D g2;

    class Tool extends AbstractAction implements MouseInputListener {
        Point o;
        Shape shape;

        Tool(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            System.out.println("Using tool " + this);
            panel.removeMouseListener(tool);
            panel.removeMouseMotionListener(tool);
            tool = this;
            panel.addMouseListener(tool);
            panel.addMouseMotionListener(tool);
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            o = e.getPoint();
            /* Right click is pressed */
            if (e.getButton() == MouseEvent.BUTTON3 && !rightButtonPressed) {
                rightButtonPressed = true;
                /* Get the selected item */
                int selectedItem = 0;
                inMenu = markingMenu.inMenu(e.getX(), e.getY(), o.getX(), o.getY());
                openMenu(selectedItem);
            }
            panel.repaint();
        }

        public void mouseReleased(MouseEvent e) {
            shape = null;

            /* Right click is released */
            if (e.getButton() == MouseEvent.BUTTON3 && rightButtonPressed) {
                rightButtonPressed = false;
                /* Remove circle shape */
                shapes.remove(menuUI.getCircle());
                /* Remove all line shapes */
                for (Line2D.Double line : menuUI.getLines()) {
                    shapes.remove(line);
                }
                /* Remove arc shape */
                shapes.remove(menuUI.getArcMenu());
                shapes.remove(menuUI.getArcSecondMenu());

                /* Set to null the Arc variable to clean the fill */
                menuUI.setToNullArc();

                int selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
                if (selectedItem == 0 && selectedSecondItem != -1) {
                    JButton seletedButton = new JButton(tools[selectedSecondItem]);
                    /* Select new tool */
                    seletedButton.doClick();
                } else if (selectedItem == 1 && selectedSecondItem != -1) {
                    /* Select a new color */
                    colorList.setSelectedIndex(selectedSecondItem);
                } else {
                    /* Other */
                    System.out.println("EXIT");
                }
            }
            panel.repaint();
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }

        private void openMenu(int selectedItem) {
            menuUI = (MarkingMenuUI) shape;
            if (menuUI == null) {
                /* Set the menu diameter to 300px */
                int diameter = 300;
                int radius = diameter / 2;
                menuUI = new MarkingMenuUI(o.getX(), o.getY(), radius, markingMenu.getNbItems());

                /* Draw the Arc showing the selected item */
                menuUI.drawSelectedItem(o.getX(), o.getY(), selectedItem);
            }
        }
    }

    private void action(Point o, MouseEvent e) {
        /* Redraw the Arc with the selected item when the mouse is dragged */
        shapes.remove(menuUI.getArcMenu());
        shapes.remove(menuUI.getArcSecondMenu());

        for (Line2D.Double line : menuUI.getLinesSecondMenu()) {
            shapes.remove(line);
        }
        menuUI.resetLinesSecondMenu();

        selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
        menuUI.drawSelectedItem(o.getX(), o.getY(), selectedItem);

        /* Submenu managment */
        inMenu = markingMenu.inMenu(e.getX(), e.getY(), o.getX(), o.getY());
        if (!inMenu && selectedItem != (nbItem - 1)) {
            switch (selectedItem) {
                case 0:
                    nbSecondMenu = 3;
                    break;
                case 1:
                    nbSecondMenu = 8;
                    break;
            }
            menuUI.drawSecondMenu(o.getX(), o.getY(), selectedItem);
            menuUI.drawLinesSecondMenu(o.getX(), o.getY(), 250, nbSecondMenu, selectedItem);
            selectedSecondItem = markingMenu.getSelectedSecondItem(e.getX(), e.getY(), o.getX(), o.getY(), nbSecondMenu);
            menuUI.drawSelectedSecondItem(o.getX(), o.getY(), selectedItem, selectedSecondItem, nbSecondMenu);
        } else {
            selectedSecondItem = -1;
        }
    }

    private Tool tools[] = {
            new Tool("Pen") {
                public void mouseDragged(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 && !rightButtonPressed) {
                        Path2D.Double path = (Path2D.Double) shape;
                        if (path == null) {
                            path = new Path2D.Double();
                            path.moveTo(o.getX(), o.getY());
                        }
                        path.lineTo(e.getX(), e.getY());
                        /* Add the shape with the selected color */
                        shapes.put(shape = path, ((ComboItem) Objects.requireNonNull(colorList.getSelectedItem())).getValue());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        action(o, e);
                    }
                    panel.repaint();
                }
            },
            new Tool("Rectangle") {
                public void mouseDragged(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 && !rightButtonPressed) {
                        Rectangle2D.Double rect = (Rectangle2D.Double) shape;

                        if (rect == null) {
                            rect = new Rectangle2D.Double(o.getX(), o.getY(), 0, 0);
                        }
                        rect.setRect(min(e.getX(), o.getX()), min(e.getY(), o.getY()), abs(e.getX() - o.getX()), abs(e.getY() - o.getY()));
                        /* Add the shape with the selected color */
                        shapes.put(shape = rect, ((ComboItem) Objects.requireNonNull(colorList.getSelectedItem())).getValue());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        action(o, e);
                    }
                    panel.repaint();
                }
            },
            new Tool("Elipse") {
                public void mouseDragged(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 && !rightButtonPressed) {
                        Ellipse2D.Double elip = (Ellipse2D.Double) shape;
                        if (elip == null) {
                            elip = new Ellipse2D.Double(o.getX(), o.getY(), 0, 0);
                        }
                        elip.setFrame(min(e.getX(), o.getX()), min(e.getY(), o.getY()), abs(e.getX() - o.getX()), abs(e.getY() - o.getY()));
                        /* Add the shape with the selected color */
                        shapes.put(shape = elip, ((ComboItem) Objects.requireNonNull(colorList.getSelectedItem())).getValue());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        action(o, e);
                    }
                    panel.repaint();
                }
            }
    };

    private Tool tool;

    private JPanel panel;

    private Paint(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));

        /* Add the possible colors in the Menu */
        colorList.addItem(new ComboItem("Black", new Color(53, 64, 80)));
        colorList.addItem(new ComboItem("Red", new Color(218, 68, 83)));
        colorList.addItem(new ComboItem("Orange", new Color(233, 87, 63)));
        colorList.addItem(new ComboItem("Yellow", new Color(246, 187, 66)));
        colorList.addItem(new ComboItem("Green", new Color(140, 193, 82)));
        colorList.addItem(new ComboItem("Indigo", new Color(55, 188, 155)));
        colorList.addItem(new ComboItem("Blue", new Color(74, 137, 220)));
        colorList.addItem(new ComboItem("Purple", new Color(125, 70, 169)));

        colorList.setMaximumSize(colorList.getPreferredSize());
        colorList.setSelectedIndex(0);

        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.BLACK);

                for (Shape shape : shapes.keySet()) {
                    /* Set the selected color for each shape */
                    g2.setColor(shapes.get(shape));
                    g2.draw(shape);
                }

                if (menuUI != null && menuUI.getArcMenu() != null) {
                    g2.setColor(new Color(190,194, 198));

                    /* Submenu managment */
                    if (!inMenu && selectedItem != (nbItem - 1)) {
                        g2.fill(menuUI.getArcSecondMenu());
                        if (selectedItem == 1) {
                            g2.setColor(colorList.getItemAt(selectedSecondItem).getValue());
                        } else {
                            g2.setColor(new Color(131, 138, 140));
                        }
                        g2.fill(menuUI.getArcSelectedSecondMenu());
                        g2.setColor(Color.BLACK);
                        for (Line2D.Double line : menuUI.getLinesSecondMenu()) {
                            g2.draw(line);
                        }
                    }

                    g2.setColor(new Color(190,194, 198));

                    /* Fill the circle shape */
                    g2.fill(menuUI.getCircle());
                    /* Draw the marking menu : Circle + Lines + Arc */
                    if (selectedItem == 2) {
                        g2.setColor(new Color(237, 85, 101));
                    } else {
                        g2.setColor(new Color(152, 163, 166));
                    }
                    g2.fill(menuUI.getArcMenu());


                    g2.setColor(Color.BLACK);
                    g2.draw(menuUI.getCircle());

                    for (Line2D.Double line : menuUI.getLines()) {
                        g2.draw(line);
                    }

                    for (int i = 0; i < markingMenu.getNbItems(); i++) {
                        /* Draw item names for each item */
                        g2.drawString(
                                markingMenu.getItemNames(i),
                                (int) (menuUI.getMenuX() + (100.0 * Math.cos((((2 * Math.PI) / markingMenu.getNbItems()) * i) + (Math.PI / markingMenu.getNbItems())))),
                                (int) (menuUI.getMenuY() + (100.0 * Math.sin((((2 * Math.PI) / markingMenu.getNbItems()) * i) + (Math.PI / markingMenu.getNbItems()))))
                        );
                    }

                    if (!inMenu && selectedItem != (nbItem - 1)) {
                        for (int i = 0; i < nbSecondMenu; i++) {
                            /* Draw item names for each item */
                            switch (selectedItem) {
                                case 0:
                                    /* Draw Tool item names */
                                    g2.drawString(
                                            markingMenu.getItemToolNames(i),
                                            (int) (menuUI.getMenuX() + (200.0 * Math.cos(((((2 * Math.PI) / markingMenu.getNbItems()) / nbSecondMenu) * i) + ((Math.PI / markingMenu.getNbItems()) / nbSecondMenu)))),
                                            (int) (menuUI.getMenuY() + (200.0 * Math.sin(((((2 * Math.PI) / markingMenu.getNbItems()) / nbSecondMenu) * i) + ((Math.PI / markingMenu.getNbItems()) / nbSecondMenu))))
                                    );
                                    break;
                                case 1:
                                    /* Draw Color item names */
                                    g2.drawString(
                                            markingMenu.getItemColorNames(i),
                                            (int) (menuUI.getMenuX() + (235.0 * Math.cos(((((2 * Math.PI) / markingMenu.getNbItems()) / nbSecondMenu) * i) + (.9*((Math.PI / markingMenu.getNbItems()) / nbSecondMenu)) + ((2 * Math.PI) / nbItem)))),
                                            (int) (menuUI.getMenuY() + (235.0 * Math.sin(((((2 * Math.PI) / markingMenu.getNbItems()) / nbSecondMenu) * i) + (.9*((Math.PI / markingMenu.getNbItems()) / nbSecondMenu)) + ((2 * Math.PI) / nbItem))))
                                    );
                                    break;
                            }

                        }
                    }
                }
            }
        };

        JToolBar toolBar = new JToolBar();
        for (AbstractAction tool : tools) {
            toolBar.add(tool);
            /* Default tool selection */
            if (tool.getValue(Action.NAME) == "Pen") {
                JButton defaultButton = new JButton(tool);
                defaultButton.doClick();
            }
        }
        toolBar.add(colorList);

        add(toolBar, BorderLayout.NORTH);

        add(panel);

        pack();
        setVisible(true);
    }

    public static void main(String argv[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Paint markingMenu = new Paint("Marking MarkingMenu");
            }
        });
    }
}

/* Inspired by https://stackoverflow.com/questions/17887927/adding-items-to-a-jcombobox */
class ComboItem {
    private String key;
    private Color value;

    ComboItem(String key, Color value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key;
    }

    Color getValue() {
        return value;
    }
}