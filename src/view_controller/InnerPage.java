package view_controller;

import java.util.Stack;
import java.awt.Point;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import model.Brush;
import model.Eraser;
import model.FillTool;
import model.History;
import model.RectSelect;
import model.TextTool;

/**
 * The InnerPage class represents a draw-able area.
 */
public class InnerPage extends Pane {
	// private List<DrawOperation> ops = new ArrayList<>();
	private double x, y; // Coordinates for drawing
	private String tool = "BRUSH"; // Current tool selected for drawing
	private Color color = Color.BLACK; // Current color selected for drawing
	private Brush brush; // The brush tool
	private FillTool fillTool;
	private GraphicsContext gc;
	private Canvas canvas;
	private Scale scaleTransform;
	private double startX, startY;
	private double endX, endY;
	private int index;
	private RectSelect rectSelect; // The rectangular select tool
	private WritableImage selectedImage;
	// Add a new Point to store the start position of the drag
	private Point dragStart = null;

	private History history;

	/**
	 * Constructor for the InnerPage class.
	 * 
	 * @param eraser An instance of the Eraser class.
	 * @param brush  An instance of the Brush class.
	 */
	public InnerPage(Eraser eraser, Brush brush, TextTool text, FillTool fillTool, RectSelect rectSelect) {
		this.setPrefSize(950, 500); // Set the preferred size of the InnerBox
		this.brush = brush;
		this.fillTool = fillTool;
		this.rectSelect = rectSelect;

		history = new History();

		ColorPicker cpLine = new ColorPicker(Color.BLACK);
		ColorPicker cpFill = new ColorPicker(Color.TRANSPARENT);
		Rectangle rect = new Rectangle();
		Circle circ = new Circle();
		Line line = new Line();
		canvas = new Canvas();
		canvas.widthProperty().bind(this.widthProperty());
		canvas.heightProperty().bind(this.heightProperty());
		// The graphics context of the canvas
		gc = canvas.getGraphicsContext2D();
		scaleTransform = new Scale(1, 1, 0, 0);
		canvas.getTransforms().add(scaleTransform);

		Shapes shapeDrawer = new Shapes(gc, this.brush);
		canvas.setFocusTraversable(true);
		// If the user uses RectSelect in Delete Mode
		canvas.setOnMouseClicked(event -> {
			if (tool.equals("DELETE")) {
				System.out.println("Click RectSelect");
				// Check for right-click and if an area has been selected
				if (event.getButton() == MouseButton.SECONDARY && rectSelect.isAreaSelected()) {
					// If it's a double click, enter delete mode
					if (event.getClickCount() == 2) {
						// Delete mode: clear and delete the selected area
						Rectangle selectedArea1 = rectSelect.getRectangle();
						gc.setFill(Color.WHITE);
						gc.fillRect(selectedArea1.getX(), selectedArea1.getY(), selectedArea1.getWidth(),
								selectedArea1.getHeight());

						// Reset the stroke color to the background color after the user has finished
						// deleting.
						gc.setStroke(Color.WHITE);
						gc.strokeRect(selectedArea1.getX(), selectedArea1.getY(), selectedArea1.getWidth(),
								selectedArea1.getHeight());
						rectSelect.deleteArea();

					}
				}
			}
		});
		// Event handler for when the mouse is pressed on the canvas
		canvas.setOnMousePressed(e -> {
			// Call performAction to capture the current state when the mouse is pressed
			performAction();
			if (tool.equals("TEXT")) {
				// Set the fill color to black
				gc.setFill(Color.BLACK);
				text.newText(gc, e.getX(), e.getY());
				performAction();
			}
			// If the current tool is "FILL"
			else if (tool.equals("FILL")) {
				fillTool.fill(gc, (int) e.getX(), (int) e.getY(), this.canvas);
			} else if (tool.equals("MOVECOPY") || tool.equals("DELETE")) {
				// Store the start position of the drag
				dragStart = new Point((int) e.getX(), (int) e.getY());
				// Temporarily ignore any transformations applied to the canvas when the user
				// adjusts the zoom level using the slider.
				Transform transform = canvas.getTransforms().isEmpty() ? null : canvas.getTransforms().get(0);
				// Check if the click is inside the selected area
				if (rectSelect.isAreaSelected() && rectSelect.getRectangle().contains(e.getX(), e.getY())) {
					rectSelect.setMoveMode(true);

					// Temporarily remove all transformations
					canvas.getTransforms().clear();

					// Store the image within the selected area
					SnapshotParameters sp = new SnapshotParameters();
					sp.setViewport(new Rectangle2D(rectSelect.getRectangle().getX(), rectSelect.getRectangle().getY(),
							rectSelect.getRectangle().getWidth(), rectSelect.getRectangle().getHeight()));
					selectedImage = canvas.snapshot(sp, null);

					// Reapply the transformation
					if (transform != null) {
						canvas.getTransforms().add(transform);
					}
				} else {
					rectSelect.setMoveMode(false);
				}
			} else if (tool.equals("SQUARE")) {
				shapeDrawer.setSquare(e.getX(), e.getY(), color, cpFill.getValue(), rect);
			} else if (tool.equals("TRIANGLE")) {
				startX = e.getX();
				startY = e.getY();
			} else if (tool.equals("CIRCLE")) {
				shapeDrawer.setCircle(e.getX(), e.getY(), color, cpFill.getValue(), circ);
			} else if (tool.equals("CURVE")) {
				startX = e.getX();
				startY = e.getY();
			} else if (tool.equals("RECTANGLE")) {
				shapeDrawer.setSquare(e.getX(), e.getY(), color, cpFill.getValue(), rect);
			} else if (tool.equals("LINE")) {
				gc.setStroke(cpLine.getValue());
				line.setStartX(e.getX());
				line.setStartY(e.getY());
				gc.setStroke(color);
			}

		});
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
			if (tool.equals("BRUSH")) {
				gc.setFill(color); // Set the drawing color to the current color
				brush.draw(gc, e.getX(), e.getY()); // Use the brush to draw
			} else if (tool.equals("ERASER")) {
				eraser.erase(gc, e.getX(), e.getY()); // Use the eraser tool to erase at the current point
			} else if ((tool.equals("MOVECOPY") || tool.equals("DELETE")) && dragStart != null) {
				// Update the selected area as the mouse is dragged
				rectSelect.selectArea(dragStart, new Point((int) e.getX(), (int) e.getY()));

			}
		});

		// Set an event handler for when the mouse is released on the canvas
		canvas.setOnMouseReleased(e -> {
			// Check which tool is currently selected
			if (tool.equals("LINE")) {
				line.setEndX(e.getX());
				line.setEndY(e.getY());
				gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
			} else if (tool.equals("SQUARE")) {
				// If the tool is "SQUARE", draw a square from the initial mouse press
				// coordinates to the current mouse coordinates
				shapeDrawer.drawSquare(rect.getX(), rect.getY(), e.getX(), e.getY(), rect);
			} else if (tool.equals("RECTANGLE")) {
				shapeDrawer.drawSquare(rect.getX(), rect.getY(), e.getX(), e.getY(), rect);
			} else if (tool.equals("CIRCLE")) {
				shapeDrawer.drawCircle(e.getX(), e.getY(), circ);
			} else if (tool.equals("TRIANGLE")) {
				shapeDrawer.drawTriangle(startX, startY, e.getX(), e.getY(), color);
			} else if (tool.equals("CURVE")) {
				endX = e.getX();
				endY = e.getY();
				double width = Math.abs(endX - startX);
				double height = Math.abs(endY - startY);
				double centerX = Math.min(startX, endX) + width / 2;
				double centerY = Math.min(startY, endY) + height / 2;
				double startAngle = 0;
				double length = 180;
				shapeDrawer.drawArc(centerX - width / 2, centerY - height / 2, width, height, startAngle, length, color,
						cpFill.getValue());
			} else if (tool.equals("MOVECOPY")) {
				dragStart = null;
				if (rectSelect.isMoveMode()) {
					// Draw the selected image at the new location only if move mode is activated
					gc.drawImage(selectedImage, e.getX() - 40, e.getY() - 40);
					// Reset move mode
					rectSelect.setMoveMode(false);
					// Reset selectedImage to null to capture a new image for the new selected area
					selectedImage = null;
				}

			} else if (tool.equals("DELETE")) {
				System.out.println("Prepare To Delete");
				// Check if an area has been selected
				if (rectSelect.isAreaSelected()) {
					// Draw a rectangle around the selected area
					Rectangle selectedArea1 = rectSelect.getRectangle();
					gc.setStroke(Color.GREY); // Set the color of the rectangle
					gc.setLineWidth(2); // Set the width of the rectangle
					gc.strokeRect(selectedArea1.getX(), selectedArea1.getY(), selectedArea1.getWidth(),
							selectedArea1.getHeight());
				}
			}
			// Call performAction to capture the current state after the drawing is
			// completed
			performAction();
		});
		cpLine.setOnAction(e -> {
			gc.setStroke(color);
		});

		// Add the canvas to the children of this InnerPage, making it visible in the
		// graphical interface
		this.getChildren().add(canvas);

		// Set padding around the InnerBox
		this.setPadding(new Insets(10));

	}

	/**
	 * Sets the current tool to the provided string.
	 * 
	 * @param tool The name of the tool to be set.
	 */
	public void setTool(String tool) {
		this.tool = tool;
	}

	/**
	 * Get the tool that is being selected
	 * 
	 * @return tool
	 */
	public String getTool() {
		return this.tool;
	}

	/**
	 * Sets the current drawing color to the provided color.
	 * 
	 * @param color The color to be set for drawing.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Get current GC
	 * 
	 * @return graphics context
	 */
	public GraphicsContext getGC() {
		return this.gc;
	}

	/**
	 * The current canvas is represented
	 * 
	 * @return the current canvas
	 */
	public Canvas getCanvas() {
		return canvas;
	}

	/**
	 * get the current width and height of canvas
	 * 
	 * @return array represents the width and height of the canvas
	 */
	public int[] getSize() {
		int[] arr = { (int) canvas.getWidth(), (int) canvas.getHeight() };
		return arr;
	}

	/**
	 * ZoomSlider that lets the user drag the slider to zoom in or out of the canvas
	 * 
	 * @param canvas The main canvas
	 * @return a slider
	 */
	public Slider zoomBySlider(InnerPage canvas) {
		Slider zoomSlider = new Slider(1, 100, 50);
		zoomSlider.setShowTickLabels(true);
		zoomSlider.setShowTickMarks(true);

		// Listen for changes on the slider's value
		zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			// Calculate the scale Amount relative to the slider position with 50 as the
			// base
			double sliderBase = 50.0;
			double scaleAmount = newValue.intValue() / sliderBase;
			// Update the scale transform with the new Amount
			scaleTransform.setX(scaleAmount);
			scaleTransform.setY(scaleAmount);
		});
		canvas.widthProperty().addListener(observable -> drawBackground());
		canvas.heightProperty().addListener(observable -> drawBackground());
		return zoomSlider;
	}

	/**
	 * Background of the canvas
	 */
	private void drawBackground() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setFill(Color.BLACK);
	}

	/**
	 * Method to make a new canvas by clearing the current canvas
	 */
	public void newPage() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		this.drawBackground();
	}

	/**
	 * Undo the last action performed on the canvas, if any.
	 */
	public void undo() {
		if (!history.isEmpty()) {
			history.undo();
			redrawCanvas();
		}
	}

	/**
	 * Re-do the last action performed on the canvas, if any.
	 */
	public void redo() {
		if (!history.isEmpty()) {
			history.redo();
			redrawCanvas();
		}
	}

	/**
	 * Performs an action on the canvas. If the canvas has content, a snapshot of
	 * the current state is taken and added to the history.
	 */
	private void performAction() {
		// Check if the canvas has any content
		boolean hasContent = canvas.snapshot(null, null) != null;
		// Only add the action to the history if there is content on the canvas
		if (hasContent) {
			// Temporarily ignore any transformations applied to the canvas when the user
			// adjusts the zoom level using the slider.
			Transform transform = canvas.getTransforms().isEmpty() ? null : canvas.getTransforms().get(0);
			// Temporarily remove all transformations
			canvas.getTransforms().clear();
			WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), null);
			// Reapply the transformation
			if (transform != null) {
				canvas.getTransforms().add(transform);
			}
			history.addAction(snapshot);
		}
	}

	/**
	 * Redraws the canvas. The current state of the canvas is fetched from the
	 * history, and if it's not null, it's drawn on the canvas.
	 */
	private void redrawCanvas() {
		// Clear the canvas
		gc.clearRect(0, 0, getWidth(), getHeight());
		// Get the current state from the history
		WritableImage currentState = history.getCurrentState();
		// Redraw the current state on the canvas
		if (currentState != null) {
			gc.drawImage(currentState, 0, 0);
		}
	}

	/**
	 * Returns the history of actions performed on the canvas.
	 * 
	 * @return the history of actions performed on the canvas
	 */
	public History getHistory() {
		return this.history;
	}

}
