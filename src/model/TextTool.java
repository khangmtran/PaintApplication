package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import view_controller.Tool;

/**
 * This class represents a tool for generating text on the canvas.
 */

public class TextTool implements Tool {
	private int size;

	/**
	 * constructor
	 * 
	 * @param size Default size for text
	 */
	public TextTool(int size) {
		this.size = size;
	}

	/**
	 * Set the size of the text. This method overrides the method in the Tool
	 * interface
	 * 
	 * @param size The size of the text
	 */
	@Override
	public void setSize(int size) {
		// TODO Auto-generated method stub
		this.size = size;
	}

	/**
	 * Get the size of the text
	 * 
	 * @return The size of the text
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return this.size;
	}

	/**
	 * Method to put text on the canvas
	 * 
	 * @param gc graphic context
	 * @param x Position at X to place text
	 * @param y Position at Y to place text
	 */
	public void newText(GraphicsContext gc, double x, double y) {
		gc.setFont(Font.font(size));
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Input Text");
		dialog.setHeaderText("Enter the text to be placed on canvas:");
		dialog.setContentText("Text:");

		dialog.showAndWait().ifPresent(text -> {
			Text tempText = new Text(text);
			tempText.setFont(gc.getFont());
			double textWidth = tempText.getLayoutBounds().getWidth();
			double textHeight = tempText.getLayoutBounds().getHeight();
			double baselineOffset = tempText.getBaselineOffset();

			gc.fillText(text, x - textWidth / 2.0, y + (baselineOffset - textHeight / 2.0) - 5);
		});
	}

}
