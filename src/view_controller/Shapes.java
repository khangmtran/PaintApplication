package view_controller;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import model.Brush;
public class Shapes {

	private GraphicsContext gc;
	// The brush tool used for drawing shapes
	private Brush brush;

	Rectangle rect = new Rectangle();
	Circle circ = new Circle();
	Ellipse elps = new Ellipse();
	Polygon triangle = new Polygon();

	public Shapes(GraphicsContext gc, Brush brush) {
		this.gc = gc;
		this.brush = brush; // Initialize the brush tool
	}

	/**
	 * Sets the current selected shape to square
	 * 
	 * @param x       mouse x
	 * @param y       mouse y
	 * @param color   current color
	 * @param fill    current fill color
	 * @param rect    rectangle object
	 */
	public void setSquare(double x, double y, Color color, Color fill, Rectangle rect) {
		gc.setStroke(color);
		gc.setFill(fill);
		rect.setX(x);
		rect.setY(y);
	}

	/**
	 * sets the current selected shape to circle
	 * 
	 * @param x      mouse x
	 * @param y      mouse y
	 * @param color  current color
	 * @param fill   current fill color
	 * @param circ   circle object
	 */
	public void setCircle(double x, double y, Color color, Color fill, Circle circ) {
		gc.setStroke(color);
		gc.setFill(fill);
		circ.setCenterX(x);
		circ.setCenterY(y);
	}

	/**
	 * Draws a sqaure on the canvas at the given coordinates
	 * 
	 * @param startX    starting mouse x
	 * @param startY    starting mouse y
	 * @param endX      ending mouse x
	 * @param endY      ending mouse y
	 * @param rect      rectangle object
	 */
	public void drawSquare(double startX, double startY, double endX, double endY, Rectangle rect) {
		rect.setWidth(Math.abs((endX - startX)));
		rect.setHeight(Math.abs((endY - startY)));

		if (startX > endX) {
			rect.setX(endX);
		}

		if (startY > endY) {
			rect.setY(endY);
		}

		gc.setLineWidth(brush.getSize());

		gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
		gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	/**
	 * Draws an arc on the canvas at the given coordinates
	 * @param x              mouse x
	 * @param y              mouse y
	 * @param w              width
	 * @param h              height
	 * @param startAngle     angle of the arc
	 * @param arcExtent      extnet of the arc
	 * @param strokeColor    current color
	 * @param fillColor      fill color
	 */
	public void drawArc(double x, double y, double w, double h, double startAngle, double arcExtent, Color strokeColor,
			Color fillColor) {
		gc.setStroke(strokeColor);
		gc.setFill(fillColor);
		gc.strokeArc(x, y, w, h, startAngle, arcExtent, ArcType.OPEN); // Change ArcType as needed
		gc.fillArc(x, y, w, h, startAngle, arcExtent, ArcType.OPEN); // Change ArcType as needed
	}

	/**
	 * Draws a circle on the canvas at the given coordiantes
	 * 
	 * @param startX     starting mouse x
	 * @param startY     starting mouse y
	 * @param circ       circle object
	 */
	public void drawCircle(double startX, double startY, Circle circ) {
		circ.setRadius((Math.abs(startX - circ.getCenterX()) + Math.abs(startY - circ.getCenterY())) / 2);

		if (circ.getCenterX() > startX) {
			circ.setCenterX(startX);
		}
		if (circ.getCenterY() > startY) {
			circ.setCenterY(startY);
		}
		gc.setLineWidth(brush.getSize());
		gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
		gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
	}

	/**
	 * Draws a traingle on the canvas at the given coordinates
	 * 
	 * @param startX       starting mouse x
	 * @param startY       starting mouse y
	 * @param endX         ending mouse x
	 * @param endY         ending mouse y
	 * @param lineColor    current color
	 */
	public void drawTriangle(double startX, double startY, double endX, double endY, Color lineColor) {
		double baseX = endX;
		double baseY = endY;

		double[] trianglePoints = { startX, startY, // top vertex
				startX - (baseX - startX), baseY, // bottom-left vertex
				baseX, baseY // bottom-right vertex
		};

		gc.setLineWidth(brush.getSize());
		gc.strokePolygon(new double[] { trianglePoints[0], trianglePoints[2], trianglePoints[4] },
				new double[] { trianglePoints[1], trianglePoints[3], trianglePoints[5] }, 3);
	}

}
