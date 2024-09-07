package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import view_controller.Tool;

/**
 * The Eraser class implements the Tool interface and represents an eraser tool.
 */
public class Eraser implements Tool {
	private int size; // The size of the eraser

	// Constructor that initializes the eraser with a given size
	public Eraser(int size) {
		this.size = size;
	}

	@Override
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public int getSize() {
		return this.size;
	}

	// Method to perform the erase operation
	public void erase(GraphicsContext gc, double x, double y) {
		// Clears a rectangular area in the canvas
		gc.setFill(Color.WHITE);
		gc.fillRect(x - size / 2.0, y - size / 2.0, size, size);
	}

	// Method to create a slider for adjusting the eraser size
	public Slider createEraserSizeSlider(Eraser eraser) {
		Slider eraserSizeSlider = new Slider(1, 100, 10); // Creates a slider with minimum value 1, maximum value 100
															// and initial value 10
		eraserSizeSlider.setShowTickLabels(true); // Enables the display of label ticks on the slider
		eraserSizeSlider.setShowTickMarks(true); // Enables the display of tick marks on the slider
		eraserSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			eraser.setSize(newValue.intValue()); // Updates the size of the eraser when the slider value changes
		});
		return eraserSizeSlider; // Returns the created slider
	}
}
