package fombaron_valette.ihm.markingmenu;

import static java.lang.Math.*;

import java.awt.geom.*;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class Paint extends JFrame {
    private HashMap<Shape, Color> shapes = new HashMap<>();
    private MarkingMenuUI menuUI;
    private JComboBox<ComboItem> colorList = new JComboBox<>();
    private int nbItem = 2;
    private MarkingMenu markingMenu = new MarkingMenu(nbItem);

    private Graphics2D g2;

    class Tool extends AbstractAction
            implements MouseInputListener {
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
            if (e.getButton() == MouseEvent.BUTTON3) {
                int selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
                openMenu(selectedItem);
            }
            panel.repaint();
        }

        public void mouseReleased(MouseEvent e) {
            shape = null;

            if (e.getButton() == MouseEvent.BUTTON3) {

                shapes.remove(menuUI.getCircle());
                for (Line2D.Double line : menuUI.getLines()) {
                    shapes.remove(line);
                }
                shapes.remove(menuUI.getArc());
                menuUI.setToNullArc();
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
                int diameter = 300;
                int radius = diameter / 2;
                menuUI = new MarkingMenuUI(o.getX(), o.getY(), radius, markingMenu.getNbItems());

                shapes.put(shape = menuUI.getCircle(), Color.BLACK);
                for (Line2D.Double line : menuUI.getLines()) {
                    shapes.put(shape = line, Color.BLACK);
                }

                menuUI.drawSelectedItem(o.getX(), o.getY(), selectedItem);
                shapes.put(menuUI.getArc(), Color.BLACK);
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
                        shapes.put(shape = path, ((ComboItem) colorList.getSelectedItem()).getValue());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        shapes.remove(menuUI.getArc());
                        int selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
                        menuUI.drawSelectedItem(o.getX(), o.getY(), selectedItem);
                        shapes.put(menuUI.getArc(), Color.BLACK);
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
                        shapes.put(shape = rect, ((ComboItem) colorList.getSelectedItem()).getValue());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        shapes.remove(menuUI.getArc());
                        int selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
                        menuUI.drawSelectedItem(o.getX(), o.getY(), selectedItem);
                        shapes.put(menuUI.getArc(), Color.BLACK);
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
                        elip.setFrame(min(e.getX(), o.getX()), min(e.getY(), o.getY()),
                                abs(e.getX() - o.getX()), abs(e.getY() - o.getY()));
                        shapes.put(shape = elip, ((ComboItem) colorList.getSelectedItem()).getValue());
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        shapes.remove(menuUI.getArc());
                        int selectedItem = markingMenu.getSelectedItem(e.getX(), e.getY(), o.getX(), o.getY());
                        menuUI.drawSelectedItem(o.getX(), o.getY(), selectedItem);
                        shapes.put(menuUI.getArc(), Color.BLACK);
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

        colorList.addItem(new ComboItem("Black", Color.BLACK));
        colorList.addItem(new ComboItem("Red", Color.RED));
        colorList.addItem(new ComboItem("Green", Color.GREEN));
        colorList.addItem(new ComboItem("Blue", Color.BLUE));
        colorList.setMaximumSize(colorList.getPreferredSize());
        colorList.setSelectedIndex(0);

        add(new JToolBar() {{
            for (AbstractAction tool : tools) {
                add(tool);
            }
            add(colorList);
        }}, BorderLayout.NORTH);

        add(panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.BLACK);

                if (menuUI != null && menuUI.getArc() != null) {
                    g2.fill(menuUI.getArc());
                    for(int i=0; i < markingMenu.getNbItems(); i++) {
                        g2.drawString(
                                String.valueOf(i),
                                (int)(menuUI.getMenuX() + (100.0 * Math.cos((((2 * Math.PI)/markingMenu.getNbItems()) * i)+(Math.PI/markingMenu.getNbItems())))),
                                (int)(menuUI.getMenuY() + (100.0 * Math.sin((((2 * Math.PI)/markingMenu.getNbItems()) * i)+(Math.PI/markingMenu.getNbItems()))))
                        );
                    }

                }

                for (Shape shape : shapes.keySet()) {
                    g2.setColor(shapes.get(shape));
                    g2.draw(shape);
                }
            }
        });

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