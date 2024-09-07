package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import view_controller.Tool;

/**
 * The Brush class implements the Tool interface and represents a brush tool.
 */
public class Brush implements Tool{
	private int size; // The size of the brush
	private Color color; // The color of the brush

	// Enum to represent different types of brushes
	public enum BrushType {
		ROUND, FLAT, FAN, ANGLE, FILBERT
	}

	private BrushType type; // The type of the brush

	// Constructor that initializes the brush with a given size and type
	public Brush(int size, BrushType type) {
		this.size = size;
		this.type = type;
	}

	@Override
	// Method to set the size of the brush
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	// Method to get the current size of the brush
	public int getSize() {
		return this.size;
	}

	// Method to get the current type of the brush
	public BrushType getType() {
		return this.type;
	}

	// Method to set the type of the brush
	public void setType(BrushType type) {
		this.type = type;
	}

	// Method to set the color of the brush
	public void setColor(Color color) {
		this.color = color;
	}

	public void draw(GraphicsContext gc, double x, double y) {
		// Set the color and line width in the graphics context
		gc.setFill(color);
		gc.setStroke(color);
		gc.setLineWidth(size);

		switch (type) {
		case ROUND:
			// Draw a round brush
			gc.fillOval(x - size / 2.0, y - size / 2.0, size, size);
			break;
		case FLAT:
			// Draw a flat brush
			gc.fillRect(x - size / 2.0, y - size / 2.0, size, size / 2.0);
			break;
		case FAN:
			// Draw a fan brush
			gc.strokeLine(x, y, x - size / 2.0, y + size);
			gc.strokeLine(x, y, x + size / 2.0, y + size);
			gc.strokeLine(x - size / 2.0, y + size, x + size / 2.0, y + size);
			break;
		case ANGLE:
			// Draw an angle brush
			gc.strokeLine(x, y, x + size / Math.sqrt(2), y + size / Math.sqrt(2));
			break;
		case FILBERT:
			// Draw a filbert brush
			gc.fillOval(x - size / 2.0, y - size / 4.0, size, size / 2.0);
			break;
		}
	}

	/**
	 * Creates a drop-down menu for selecting the brush type.
	 *
	 * @return A ComboBox<BrushType> representing a drop-down menu for selecting the
	 *         brush type.
	 */
	public ComboBox<BrushType> createBrushTypeComboBox() {
		ComboBox<BrushType> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(BrushType.values()); // Add all brush types to the drop-down menu
		comboBox.setValue(type); // Set the initial value to the current type of the brush
		comboBox.setOnAction(e -> {
			// Update the type of the brush when a new type is selected from the drop-down menu
			setType(comboBox.getValue());

		});
		return comboBox; // Return the created drop-down menu
	}

	/**
	 * Method to create a drop-down menu for selecting the brush size.
	 * 
	 * @return A ComboBox<Integer> representing a drop-down menu for selecting the
	 *         brush size.
	 */
	public ComboBox<Integer> createBrushSizeComboBox() {
		ComboBox<Integer> comboBox = new ComboBox<>();
		// Add odd sizes from 1 through 25 to the drop-down menu
		for (int i = 1; i <= 26; i += 2) {
			comboBox.getItems().add(i);
		}

		comboBox.setValue(size); // Set the initial value to the current size of the brush
		comboBox.setStyle("-fx-pref-width: 70;"); // Set the preferred width of the ComboBox to 70
		comboBox.setOnAction(e -> {
			// Update the size of the brush when a new size is selected from the drop-down menu
			setSize(comboBox.getValue());
		});

		return comboBox; // Return the created drop-down menu
	}

}
