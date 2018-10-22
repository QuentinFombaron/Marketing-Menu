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
    private int nbItem = 8;
    /* Number of item in the submenu */
    private int nbSecondMenu = 3;
    private MarkingMenu markingMenu = new MarkingMenu(nbItem);
    private boolean inMenu = true;

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
            if (e.getButton() == MouseEvent.BUTTON3) {
                /* Get the selected item */
                int selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
                inMenu = markingMenu.inMenu(e.getX(), e.getY(), o.getX(), o.getY());
                openMenu(selectedItem);
            }
            panel.repaint();
        }

        public void mouseReleased(MouseEvent e) {
            shape = null;

            /* Right click is released */
            if (e.getButton() == MouseEvent.BUTTON3) {
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
                if (selectedItem >= 0 && selectedItem < 4) {
                    /* Select a new color */
                    colorList.setSelectedIndex(selectedItem);
                } else if (selectedItem >= 4 && selectedItem < 7) {
                    JButton seletedButton = new JButton(tools[selectedItem - 4]);
                    /* Select new tool */
                    seletedButton.doClick();
                } else {
                    /* Other */
                    System.out.println("OTHER ITEM");
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

    private Tool tools[] = {
            new Tool("Pen") {
                public void mouseDragged(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Path2D.Double path = (Path2D.Double) shape;
                        if (path == null) {
                            path = new Path2D.Double();
                            path.moveTo(o.getX(), o.getY());
                        }
                        path.lineTo(e.getX(), e.getY());
                        /* Add the shape with the selected color */
                        shapes.put(shape = path, ((ComboItem) colorList.getSelectedItem()).getValue());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        /* Redraw the Arc with the selected item when the mouse is dragged */
                        shapes.remove(menuUI.getArcMenu());
                        shapes.remove(menuUI.getArcSecondMenu());

                        for (Line2D.Double line : menuUI.getLinesSecondMenu()) {
                            shapes.remove(line);
                        }
                        menuUI.resetLinesSecondMenu();

                        int selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
                        menuUI.drawSelectedItem(o.getX(), o.getY(), selectedItem);

                        /* Submenu managment */
                        inMenu = markingMenu.inMenu(e.getX(), e.getY(), o.getX(), o.getY());
                        if (!inMenu) {
                            menuUI.drawSecondMenu(o.getX(), o.getY(), selectedItem);
                            menuUI.drawLinesSecondMenu(o.getX(), o.getY(), 250, nbSecondMenu, selectedItem);
                            int selectedSecondItem = markingMenu.getSelectedSecondItem(e.getX(), e.getY(), o.getX(), o.getY(), nbSecondMenu);
                            menuUI.drawSelectedSecondItem(o.getX(), o.getY(), selectedItem,selectedSecondItem, nbSecondMenu);
                        }
                    }
                    panel.repaint();
                }
            },
            new Tool("Rectangle") {
                public void mouseDragged(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Rectangle2D.Double rect = (Rectangle2D.Double) shape;

                        if (rect == null) {
                            rect = new Rectangle2D.Double(o.getX(), o.getY(), 0, 0);
                        }
                        rect.setRect(min(e.getX(), o.getX()), min(e.getY(), o.getY()), abs(e.getX() - o.getX()), abs(e.getY() - o.getY()));
                        /* Add the shape with the selected color */
                        shapes.put(shape = rect, ((ComboItem) colorList.getSelectedItem()).getValue());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        /* Redraw the Arc with the selected item when the mouse is dragged */
                        shapes.remove(menuUI.getArcMenu());
                        shapes.remove(menuUI.getArcSecondMenu());

                        for (Line2D.Double line : menuUI.getLinesSecondMenu()) {
                            shapes.remove(line);
                        }
                        menuUI.resetLinesSecondMenu();

                        int selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
                        menuUI.drawSelectedItem(o.getX(), o.getY(), selectedItem);

                        /* Submenu managment */
                        inMenu = markingMenu.inMenu(e.getX(), e.getY(), o.getX(), o.getY());
                        if (!inMenu) {
                            menuUI.drawSecondMenu(o.getX(), o.getY(), selectedItem);
                            menuUI.drawLinesSecondMenu(o.getX(), o.getY(), 250, nbSecondMenu, selectedItem);
                            int selectedSecondItem = markingMenu.getSelectedSecondItem(e.getX(), e.getY(), o.getX(), o.getY(), nbSecondMenu);
                            menuUI.drawSelectedSecondItem(o.getX(), o.getY(), selectedItem,selectedSecondItem, nbSecondMenu);
                        }
                    }
                    panel.repaint();
                }
            },
            new Tool("Elipse") {
                public void mouseDragged(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Ellipse2D.Double elip = (Ellipse2D.Double) shape;
                        if (elip == null) {
                            elip = new Ellipse2D.Double(o.getX(), o.getY(), 0, 0);
                        }
                        elip.setFrame(min(e.getX(), o.getX()), min(e.getY(), o.getY()), abs(e.getX() - o.getX()), abs(e.getY() - o.getY()));
                        /* Add the shape with the selected color */
                        shapes.put(shape = elip, ((ComboItem) colorList.getSelectedItem()).getValue());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        /* Redraw the Arc with the selected item when the mouse is dragged */
                        shapes.remove(menuUI.getArcMenu());
                        shapes.remove(menuUI.getArcSecondMenu());

                        for (Line2D.Double line : menuUI.getLinesSecondMenu()) {
                            shapes.remove(line);
                        }
                        menuUI.resetLinesSecondMenu();

                        int selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
                        menuUI.drawSelectedItem(o.getX(), o.getY(), selectedItem);

                        /* Submenu managment */
                        inMenu = markingMenu.inMenu(e.getX(), e.getY(), o.getX(), o.getY());
                        if (!inMenu) {
                            menuUI.drawSecondMenu(o.getX(), o.getY(), selectedItem);
                            menuUI.drawLinesSecondMenu(o.getX(), o.getY(), 250, nbSecondMenu, selectedItem);
                            int selectedSecondItem = markingMenu.getSelectedSecondItem(e.getX(), e.getY(), o.getX(), o.getY(), nbSecondMenu);
                            menuUI.drawSelectedSecondItem(o.getX(), o.getY(), selectedItem,selectedSecondItem, nbSecondMenu);
                        }
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
        colorList.addItem(new ComboItem("Black", Color.BLACK));
        colorList.addItem(new ComboItem("Red", Color.RED));
        colorList.addItem(new ComboItem("Green", Color.GREEN));
        colorList.addItem(new ComboItem("Blue", Color.BLUE));
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
                    g2.setColor(Color.LIGHT_GRAY);

                    /* Submenu managment */
                    if (!inMenu) {
                        g2.fill(menuUI.getArcSecondMenu());
                        g2.setColor(Color.DARK_GRAY);
                        g2.fill(menuUI.getArcSelectedSecondMenu());
                        g2.setColor(Color.BLACK);
                        for (Line2D.Double line : menuUI.getLinesSecondMenu()) {
                            g2.draw(line);
                        }
                    }

                    g2.setColor(Color.LIGHT_GRAY);

                    /* Fill the circle shape */
                    g2.fill(menuUI.getCircle());
                    /* Draw the marking menu : Circle + Lines + Arc */
                    g2.setColor(Color.GRAY);
                    g2.fill(menuUI.getArcMenu());



                    g2.setColor(Color.BLACK);
                    g2.draw(menuUI.getCircle());

                    for (Line2D.Double line : menuUI.getLines()) {
                        g2.draw(line);
                    }

                    List<String> itemNames = new LinkedList<>();
                    itemNames.add("Black");
                    itemNames.add("Red");
                    itemNames.add("Green");
                    itemNames.add("Blue");
                    itemNames.add("Pen");
                    itemNames.add("Rect.");
                    itemNames.add("Elip.");
                    itemNames.add("Other");

                    for (int i = 0; i < markingMenu.getNbItems(); i++) {
                        /* Draw item names for each item */
                        g2.drawString(
                                itemNames.get(i),
                                (int) (menuUI.getMenuX() + (100.0 * Math.cos((((2 * Math.PI) / markingMenu.getNbItems()) * i) + (Math.PI / markingMenu.getNbItems())))),
                                (int) (menuUI.getMenuY() + (100.0 * Math.sin((((2 * Math.PI) / markingMenu.getNbItems()) * i) + (Math.PI / markingMenu.getNbItems()))))
                        );
                    }
                }
            }
        };

        JToolBar toolBar = new JToolBar();
        for (AbstractAction tool : tools) {
            toolBar.add(tool);
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