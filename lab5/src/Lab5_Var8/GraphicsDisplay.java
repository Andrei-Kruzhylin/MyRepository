package Lab5_Var8;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {

    private boolean showAxis = true;
    private boolean showMarkers = true;
    private ArrayList<Double[]> graphicsData;
    private ArrayList<Double[]> originalData;
    private static DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
    private double[][] viewport = new double[2][2];
    private boolean scaleMode = false;
    private boolean changeMode = false;
    private int selectedMarker = -1;
    private ArrayList<double[][]> undoHistory = new ArrayList(5);
    private Rectangle2D.Double selectionRect = new Rectangle2D.Double();
    private double[] originalPoint = new double[2];
    private BasicStroke selectionStroke;
    private Font labelsFont;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double scale;
    private double scaleX;
    private double scaleY;
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private Font axisFont;

    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, new float[] {4, 1, 2, 1, 2, 1, 4, 1, 1}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
        selectionStroke = new BasicStroke(1.0f, 0, 0, 10.0f, new float[]{10.0f, 10.0f}, 0.0f);
        labelsFont = new Font("Serif", 0, 20);
        formatter.setMaximumFractionDigits(5);
        this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseMotionHandler());
    }


    public void displayGraphics(ArrayList<Double[]> graphicsData) {
        this.graphicsData = graphicsData;
        this.originalData = new ArrayList(graphicsData.size());
        for (Double[] point : graphicsData) {
            Double[] newPoint = new Double[]{new Double(point[0]), new Double(point[1])};
            this.originalData.add(newPoint);
        }
        this.minX = graphicsData.get(0)[0];
        this.maxX = graphicsData.get(graphicsData.size() - 1)[0];
        this.maxY = this.minY = graphicsData.get(0)[1].doubleValue();
        for (int i = 1; i < graphicsData.size(); ++i) {
            if (graphicsData.get(i)[1] < this.minY) {
                this.minY = graphicsData.get(i)[1];
            }
            if (!(graphicsData.get(i)[1] > this.maxY)) continue;
            this.maxY = graphicsData.get(i)[1];
        }
        this.zoomToRegion(this.minX, this.maxY, this.maxX, this.minY);
    }


    public void zoomToRegion(double x1, double y1, double x2, double y2) {
        this.viewport[0][0] = x1;
        this.viewport[0][1] = y1;
        this.viewport[1][0] = x2;
        this.viewport[1][1] = y2;
        this.repaint();
    }


    private void paintSelection(Graphics2D canvas) {
        if (!this.scaleMode) {
            return;
        }
        canvas.setStroke(selectionStroke);
        canvas.setColor(Color.BLACK);
        canvas.draw(selectionRect);
    }

    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.size() == 0) return;
        minX = graphicsData.get(0)[0];
        maxX = graphicsData.get(graphicsData.size() - 1)[0];
        minY = graphicsData.get(0)[1];
        maxY = minY;
        for (int i = 1; i < graphicsData.size(); i++) {
            if (graphicsData.get(i)[1] < minY) {
                minY = graphicsData.get(i)[1];
            }
            if (graphicsData.get(i)[1] > maxY) {
                maxY = graphicsData.get(i)[1];
            }
        }

        this.scaleX = this.getSize().getWidth() / (this.viewport[1][0] - this.viewport[0][0]);
        this.scaleY = this.getSize().getHeight() / (this.viewport[0][1] - this.viewport[1][1]);
        scale = Math.min(scaleX, scaleY);
        if (scale == scaleX) {
            double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {
            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }
        Graphics2D canvas = (Graphics2D) g;
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if (showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if (showMarkers) paintMarkers(canvas);
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        paintLabels(canvas);
        paintSelection(canvas);
    }

    protected void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(this.graphicsStroke);
        canvas.setColor(Color.RED);

        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.size(); i++) {
            Point2D.Double point = xyToPoint(graphicsData.get(i)[0], graphicsData.get(i)[1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            } else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);
    }

    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(this.markerStroke);

        for (Double[] point : graphicsData) {
            GeneralPath path = new GeneralPath();
            Point2D.Double center = xyToPoint(point[0], point[1]);
            canvas.setColor(Color.RED);
            if (unorderedValues(point[1]) )
            {
                canvas.setColor(Color.BLUE);			//инеаче - синий
            }
            path.append(new Line2D.Double(center.getX() - 5.5, center.getY() - 0.0, center.getX() + 5.5, center.getY() - 0.0), true);
            path.append(new Line2D.Double(center.getX() + 5.5, center.getY() - 0.0, center.getX() + 0.0, center.getY() - 0.0), true);
            path.append(new Line2D.Double(center.getX() + 0.0, center.getY() - 5.5, center.getX() + 0.0, center.getY() + 5.5), true);
            path.append(new Line2D.Double(center.getX() + 0.0, center.getY() + 5.5, center.getX() + 0.0, center.getY() + 0.0), true);
            path.append(new Line2D.Double(center.getX() - 5.5, center.getY() - 5.5, center.getX() + 5.5, center.getY() + 5.5), true);
            path.append(new Line2D.Double(center.getX() + 5.5, center.getY() + 5.5, center.getX() + 0.0, center.getY() + 0.0), true);
            path.append(new Line2D.Double(center.getX() - 5.5, center.getY() + 5.5, center.getX() + 5.5, center.getY() - 5.5), true);
            path.append(new Line2D.Double(center.getX() + 5.5, center.getY() - 5.5, center.getX() + 0.0, center.getY() - 0.0), true);
            canvas.draw(path);
        }
    }

    boolean unorderedValues(double value)
    {
        double average = 0.0;
        double prev = 0.0;
        double now;
        int numbers = 1;
        for (Double[] point : graphicsData) {
            now = point[1];
            if (now > prev) {
                average += point[1];
                numbers++;
            }
            prev = now;
        }
        average /= numbers;
        if (value > 2*average) {return true;}
        else{return false;}

    }

    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(this.axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();
        if (minX <= 0.0 && maxX >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY),
                    xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5,
                    arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10,
                    arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("y", (float) labelPos.getX() + 10,
                    (float) (labelPos.getY() - bounds.getY()));
        }
        if (minY <= 0.0 && maxY >= 0.0) {

            canvas.draw(new Line2D.Double(xyToPoint(minX, 0),
                    xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20,
                    arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(),
                    arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(viewport[1][0], 0.0);
            canvas.drawString("x", (float) (labelPos.getX() - bounds.getWidth() - 10), (float) (labelPos.getY() + bounds.getY()));
            arrow.moveTo(labelPos.getX(), labelPos.getY());
            arrow.lineTo(labelPos.getX() - 20, labelPos.getY() - 5);
            arrow.lineTo(labelPos.getX()-20, labelPos.getY() + 5);
            canvas.draw(arrow);
            canvas.fill(arrow);
        }
    }


    private void paintLabels(Graphics2D canvas) {
        Rectangle2D bounds;
        String label;
        Point2D.Double point;
        FontRenderContext context = canvas.getFontRenderContext();
        if (this.selectedMarker >= 0) {
            point = this.xyToPoint(this.graphicsData.get(this.selectedMarker)[0], this.graphicsData.get(this.selectedMarker)[1]);
            label = "X=" + formatter.format(this.graphicsData.get(this.selectedMarker)[0]) + ", Y=" + formatter.format(this.graphicsData.get(this.selectedMarker)[1]);
            bounds = this.labelsFont.getStringBounds(label, context);
            canvas.setColor(Color.BLACK);
            canvas.drawString(label, (float)(point.getX() + 5.0), (float)(point.getY() - bounds.getHeight()));
        }
    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - this.viewport[0][0];
        double deltaY = this.viewport[0][1] - y;
        return new Point2D.Double(deltaX * this.scaleX, deltaY * this.scaleY);
    }


    protected double[] PointToxy(int x, int y) {
        return new double[]{this.viewport[0][0] + (double)x / this.scaleX, this.viewport[0][1] - (double)y / this.scaleY};
    }

    protected int findSelectedPoint(int x, int y) {
        if (this.graphicsData == null) {
            return -1;
        }
        int pos = 0;
        for (Double[] point : this.graphicsData) {
            Point2D.Double screenPoint = this.xyToPoint(point[0], point[1]);
            double distance = (screenPoint.getX() - (double)x) * (screenPoint.getX() - (double)x) + (screenPoint.getY() - (double)y) * (screenPoint.getY() - (double)y);
            if (distance < 100.0) {
                return pos;
            }
            ++pos;
        }
        return -1;
    }

public class MouseHandler extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent ev) {
        if (ev.getButton() == 3) {
            if (GraphicsDisplay.this.undoHistory.size() > 0) {
                GraphicsDisplay.this.viewport = (double[][])GraphicsDisplay.this.undoHistory.get(GraphicsDisplay.this.undoHistory.size() - 1);
                GraphicsDisplay.this.undoHistory.remove(GraphicsDisplay.this.undoHistory.size() - 1);
            }
            GraphicsDisplay.this.repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent ev) {
        if (ev.getButton() != 1) {
            return;
        }
        GraphicsDisplay.this.selectedMarker = GraphicsDisplay.this.findSelectedPoint(ev.getX(), ev.getY());
        GraphicsDisplay.this.originalPoint = GraphicsDisplay.this.PointToxy(ev.getX(), ev.getY());
        GraphicsDisplay.this.scaleMode = true;
        GraphicsDisplay.this.setCursor(Cursor.getPredefinedCursor(5));
        GraphicsDisplay.this.selectionRect.setFrame(ev.getX(), ev.getY(), 1.0, 1.0);
    }

    @Override
    public void mouseReleased(MouseEvent ev) {
        if (ev.getButton() != 1) {
            return;
        }
        GraphicsDisplay.this.scaleMode = false;
        double[] finalPoint = GraphicsDisplay.this.PointToxy(ev.getX(), ev.getY());
        GraphicsDisplay.this.undoHistory.add(GraphicsDisplay.this.viewport);
        GraphicsDisplay.this.viewport = new double[2][2];
        GraphicsDisplay.this.zoomToRegion(GraphicsDisplay.this.originalPoint[0], GraphicsDisplay.this.originalPoint[1], finalPoint[0], finalPoint[1]);
        GraphicsDisplay.this.repaint();
    }
}

    public class MouseMotionHandler implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent ev) {
            GraphicsDisplay.this.selectedMarker = GraphicsDisplay.this.findSelectedPoint(ev.getX(), ev.getY());
            if (GraphicsDisplay.this.selectedMarker >= 0) {
                GraphicsDisplay.this.setCursor(Cursor.getPredefinedCursor(8));
            } else {
                GraphicsDisplay.this.setCursor(Cursor.getPredefinedCursor(0));
            }
            GraphicsDisplay.this.repaint();
        }

        @Override
        public void mouseDragged(MouseEvent ev) {
            double height;
            double width = (double)ev.getX() - GraphicsDisplay.this.selectionRect.getX();
            if (width < 5.0) {
                width = 5.0;
            }
            if ((height = (double)ev.getY() - GraphicsDisplay.this.selectionRect.getY()) < 5.0) {
                height = 5.0;
            }
            GraphicsDisplay.this.selectionRect.setFrame(GraphicsDisplay.this.selectionRect.getX(), GraphicsDisplay.this.selectionRect.getY(), width, height);
            GraphicsDisplay.this.repaint();
        }
    }
}

