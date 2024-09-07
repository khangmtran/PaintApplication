package model;
import java.awt.Point;
import javafx.scene.shape.Rectangle;

/**
 * This class is for selecting a rectangle area on a canvas.
 */
public class RectSelect {
	private Rectangle rectangle; // The selected rectangle
	private boolean areaSelected; // If an area has been selected
	private boolean moveMode; // If the move mode is active

	/**
	 * This is the constructor. It sets up a new rectangle with no area.
	 */
	public RectSelect() {
		this.rectangle = new Rectangle(0, 0, 0, 0); 
		this.areaSelected = false;
	}

	/**
	 * Selects a rectangular area on the canvas using two points: start and end.
	 * The rectangle's width and height are calculated based on these points.
	 *
	 * @param start The point where the user first clicked.
	 * @param end   The point where the user released the click.
	 */
	public void selectArea(Point start, Point end) {
		this.rectangle.setX(start.getX());
		this.rectangle.setY(start.getY());
		this.rectangle.setWidth(Math.abs(end.getX() - start.getX()));
		this.rectangle.setHeight(Math.abs(end.getY() - start.getY()));
		// This will tell us that the user has now created a selected area to
		// either move, delete, or copy
		this.areaSelected = true;
	}

	/**
	 * This method moves the selected area by a given amount.
	 * 
	 * @param delta The amount to move
	 */
	public void moveArea(Point delta) {
		this.rectangle.setX(this.rectangle.getX() + delta.getX());
		this.rectangle.setY(this.rectangle.getY() + delta.getY());
	}

	/**
	 * This method deletes the selected area.
	 */
	public void deleteArea() {
		this.rectangle = new Rectangle(0, 0, 0, 0);
		this.areaSelected = false;
	}

	/**
	 * This method checks if an area has been selected.
	 * 
	 * @return true if an area has been selected, false otherwise
	 */
	public boolean isAreaSelected() {
		return this.areaSelected;
	}

	/**
	 * This method gets the selected rectangle.
	 * 
	 * @return The selected rectangle
	 */
	public Rectangle getRectangle() {
		return this.rectangle;
	}

	/**
	 * This method sets the move mode.
	 * 
	 * @param moveMode The new move mode
	 */
	public void setMoveMode(boolean moveMode) {
		this.moveMode = moveMode;
	}

	/**
	 * This method checks if the move mode is active.
	 * 
	 * @return true if the move mode is active, false otherwise
	 */
	public boolean isMoveMode() {
		return this.moveMode;
	}

}
