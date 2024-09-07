package model;

import java.awt.Point;
import java.util.Stack;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;

/**
 * This class represents a fill tool for a canvas.
 * It uses the flood fill algorithm to fill an area of the canvas with a specified color.
 */
public class FillTool {
    // The color to fill with ( default color ) 
    private Color color = Color.BLACK;

    /**
     * Constructs a new FillTool with the specified color.
     * @param color the color to fill with
     */
    public FillTool(Color color) {
        this.color = color;
    }

    /**
     * Fills an area of the canvas starting from the specified point.
     * If the color at the start point is the same as the fill color, it does nothing.
     * 
     * @param gc the GraphicsContext of the canvas
     * @param startX the x-coordinate of the start point
     * @param startY the y-coordinate of the start point
     */
    public void fill(GraphicsContext gc, int startX, int startY, Canvas canvas) {
    	// Store the current transformation matrix
        Transform transform = canvas.getTransforms().isEmpty() ? null : canvas.getTransforms().get(0);

        // Temporarily remove all transformations
        canvas.getTransforms().clear();

        // Take a snapshot of the current state of the canvas
        WritableImage writableImage = canvas.snapshot(null, null);

        // Reapply the transformation
        if (transform != null) {
            canvas.getTransforms().add(transform);
        }

        // Create a PixelReader from the snapshot. 
        PixelReader pixelReader = writableImage.getPixelReader();

        // Create a PixelWriter from the snapshot. 
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        // Get the color of the pixel at the start point
        Color targetColor = pixelReader.getColor(startX, startY);

        // If the target color is the same as the fill color, return immediately
        if (targetColor.equals(color)) {
            return;
        }

        // Get the width and height of the whole canvas
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        
        // Perform the flood fill
        floodFill(pixelReader, pixelWriter, startX, startY, targetColor, width, height);

        // Draw the updated image onto the canvas
        gc.drawImage(writableImage, 0, 0);
    }


    /**
     * Performs the flood fill algorithm on the image.
     * 
     * @param pixelReader the PixelReader of the image
     * @param pixelWriter the PixelWriter of the image
     * @param startX the x-coordinate of the start point
     * @param startY the y-coordinate of the start point
     * @param targetColor the color to be replaced
     * @param width the width of the image
     * @param height the height of the image
     */
    private void floodFill(PixelReader pixelReader, PixelWriter pixelWriter, int startX, int startY, Color targetColor, int width, int height) {
        // Create a stack to hold the points to be processed.
        Stack<Point> stack = new Stack<>();
        // Push the start point onto the stack.
        stack.push(new Point(startX, startY));

        // Continue until the stack is empty, meaning we've processed all points.
        while (!stack.isEmpty()) {
            // Pop the next point off the stack.
            Point point = stack.pop();
            int x = point.x;
            int y = point.y;

            // If the current pixel is within the image bounds and its color is the target color
            if (x >= 0 && x < width && y >= 0 && y < height && pixelReader.getColor(x, y).equals(targetColor)) {
                // Set the color of the current pixel to the fill color
                pixelWriter.setColor(x, y, color);
                // Push the neighboring pixels onto the stack to be processed in subsequent iterations.
                // This is how the flood fill "spreads" to the rest of the image.
                stack.push(new Point(x + 1, y)); // Right neighbor
                stack.push(new Point(x - 1, y)); // Left neighbor
                stack.push(new Point(x, y + 1)); // Bottom neighbor
                stack.push(new Point(x, y - 1)); // Top neighbor
            }
        }
    }

    /**
     * Sets the fill color.
     * 
     * @param color the new fill color
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
