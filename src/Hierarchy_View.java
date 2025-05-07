package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.*;

public class Hierarchy_View extends JPanel {

    private ZoomablePanel zoomablePanel;

    public Hierarchy_View(Employee rootEmployee) {
        setLayout(new BorderLayout());

        zoomablePanel = new ZoomablePanel();
        zoomablePanel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane(zoomablePanel);
        add(scrollPane, BorderLayout.CENTER);

        // Alle Employees laden
        ArrayList<Employee> allEmployees = DB_API.getAllEmployees();

        // Map für schnellen Zugriff auf Employees per ID
        Map<Integer, Employee> idToEmployee = new HashMap<>();
        for (Employee e : allEmployees) {
            idToEmployee.put(e.getId(), e);
        }

        // Baue die Management-Kette (von root bis zum Top-Manager)
        ArrayList<Employee> managementChain = new ArrayList<>();
        Employee current = rootEmployee;
        while (current != null) {
            managementChain.add(current);
            int mgrId = current.getManagerId();
            current = idToEmployee.get(mgrId); // null wenn kein Eintrag (oberster Chef)
        }

        // Positioniere alle in der Mitte, gestapelt von unten (root) nach oben
        int panelWidth = 200;
        int panelHeight = 100;
        int centerX = 900;
        int startY = 700;

        for (int i = 0; i < managementChain.size(); i++) {
            Employee emp = managementChain.get(i);
            Hierarchy_View_Single view = new Hierarchy_View_Single(emp);
            int x = centerX - panelWidth / 2;
            int y = startY - i * (panelHeight + 40); // Abstand zwischen Kästen

            view.setBounds(x, y, panelWidth, panelHeight);
            zoomablePanel.add(view);
        }

        zoomablePanel.revalidate();
        zoomablePanel.repaint();
    }

    // ZoomablePanel bleibt unverändert wie oben
    // ==== Inner Class for Zoom & Pan Panel ====
    private static class ZoomablePanel extends JPanel {

        private double scale = 1.0;
        private Point dragStartScreen;
        private Point dragStartOffset = new Point(0, 0);
        private Point currentOffset = new Point(0, 0);

        public ZoomablePanel() {
            setPreferredSize(new Dimension(2000, 2000)); // großzügige Fläche

            // Zoom per STRG + Mausrad
            addMouseWheelListener(e -> {
                if (e.isControlDown()) {
                    double delta = -0.05 * e.getWheelRotation();
                    scale += delta;
                    scale = Math.max(0.2, Math.min(scale, 5.0));
                    revalidate();
                    repaint();
                }
            });

            // Drag/Pan per Maus
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    dragStartScreen = e.getPoint();
                    dragStartOffset = new Point(currentOffset);
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    int dx = e.getX() - dragStartScreen.x;
                    int dy = e.getY() - dragStartScreen.y;
                    currentOffset = new Point(dragStartOffset.x + dx, dragStartOffset.y + dy);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Apply translation and scaling
            AffineTransform transform = new AffineTransform();
            transform.translate(currentOffset.x, currentOffset.y);
            transform.scale(scale, scale);
            g2.setTransform(transform);

            // Draw all components (they are still managed manually)
            super.paintChildren(g2);
        }

        @Override
        protected void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            // same transform again for children
            AffineTransform transform = new AffineTransform();
            transform.translate(currentOffset.x, currentOffset.y);
            transform.scale(scale, scale);
            g2.setTransform(transform);

            super.paintChildren(g2);
            g2.dispose();
        }
    }

}
