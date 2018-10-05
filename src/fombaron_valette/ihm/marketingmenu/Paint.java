//////////////////////////////////////////////////////////////////////////////
// file    : Paint.java
// content : basic painting app
//////////////////////////////////////////////////////////////////////////////

package fombaron_valette.ihm.marketingmenu;

/* imports *****************************************************************/

import static java.lang.Math.*;

import java.awt.geom.Ellipse2D;
import java.util.Vector;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class Paint extends JFrame implements ActionListener {
    private Vector<Shape> shapes = new Vector<Shape>();

    private JComboBox<ComboItem> colorList;
    private Graphics2D g2;

    class Tool extends AbstractAction
            implements MouseInputListener {
        Point o;
        Shape shape;
        Tool(String name) { super(name); }
        public void actionPerformed(ActionEvent e) {
            System.out.println("Using tool " + this);
            panel.removeMouseListener(tool);
            panel.removeMouseMotionListener(tool);
            tool = this;
            panel.addMouseListener(tool);
            panel.addMouseMotionListener(tool);
        }
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) { o = e.getPoint(); }
        public void mouseReleased(MouseEvent e) { shape = null; }
        public void mouseDragged(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {}
    }

    private Tool tools[] = {
            new Tool("Pen") {
                public void mouseDragged(MouseEvent e) {
                    Path2D.Double path = (Path2D.Double)shape;
                    if(path == null) {
                        path = new Path2D.Double();
                        path.moveTo(o.getX(), o.getY());
                        shapes.add(shape = path);
                    }
                    path.lineTo(e.getX(), e.getY());
                    panel.repaint();
                }
            },
            new Tool("Rectangle") {
                public void mouseDragged(MouseEvent e) {
                    Rectangle2D.Double rect = (Rectangle2D.Double)shape;
                    if(rect == null) {
                        rect = new Rectangle2D.Double(o.getX(), o.getY(), 0, 0);
                        shapes.add(shape = rect);
                    }
                    rect.setRect(min(e.getX(), o.getX()), min(e.getY(), o.getY()),
                            abs(e.getX()- o.getX()), abs(e.getY()- o.getY()));
                    panel.repaint();
                }
            },
            new Tool("Elipse") {
                public void mouseDragged(MouseEvent e) {
                    Ellipse2D.Double elip = (Ellipse2D.Double)shape;
                    if(elip == null) {
                        elip = new Ellipse2D.Double(o.getX(), o.getY(), 0, 0);
                        shapes.add(shape = elip);
                    }
                    elip.setFrame(min(e.getX(), o.getX()), min(e.getY(), o.getY()),
                            abs(e.getX()- o.getX()), abs(e.getY()- o.getY()));
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


        JComboBox<ComboItem> colorList = new JComboBox<>();
        colorList.addItem(new ComboItem("Black", Color.BLACK));
        colorList.addItem(new ComboItem("Red", Color.RED));
        colorList.addItem(new ComboItem("Blue", Color.BLUE));
        colorList.addItem(new ComboItem("Green", Color.GREEN));
        colorList.setMaximumSize(colorList.getPreferredSize());
        colorList.setSelectedIndex(0);
        colorList.addActionListener(this);

        add(new JToolBar() {{
            for(AbstractAction tool: tools) {
                add(tool);
            }
            add(colorList);
        }}, BorderLayout.NORTH);
        add(panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());

                ComboItem colorItem = (ComboItem)colorList.getSelectedItem();
                if (colorItem != null) {
                    g2.setColor(colorItem.getValue());
                } else {
                    g2.setColor(Color.BLACK);
                }

                for(Shape shape: shapes) {
                    g2.draw(shape);
                }
            }
        });

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox colorList = (JComboBox)e.getSource();
        ComboItem colorItem = (ComboItem)colorList.getSelectedItem();
        if (colorItem != null) {
            g2.setColor(colorItem.getValue());
        } else {
            g2.setColor(Color.BLACK);
        }
        panel.repaint();
    }

    public static void main(String argv[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Paint markingMenu = new Paint("Marking Menu");
            }
        });
    }
}

/* Inspired by https://stackoverflow.com/questions/17887927/adding-items-to-a-jcombobox */
class ComboItem {
    private String key;
    private Color value;

    public ComboItem(String key, Color value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key;
    }

    public String getKey() {
        return key;
    }

    public Color getValue() {
        return value;
    }
}
