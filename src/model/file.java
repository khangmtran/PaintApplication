package model;
import java.io.File;

import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import view_controller.InnerPage;

/*
 * Class file that handles all the operations for saving and creating 
 * drawings from the app
 */
public class file {
	private InnerPage page;
	
	/**
	 * Constructor
	 * 
	 * @param iPage  Innerpage being used
	 */
	public file(InnerPage iPage) {
		page = iPage;
	}
	
	/**
	 * Allows user to choose an image file from their deivce and
	 * draws it on the canvas
	 * 
	 * @param stage   current main stage
	 */
	public void drawImage(Stage stage) {	
		GraphicsContext gc = page.getGC();
		FileChooser choose = new FileChooser();
		File f = choose.showOpenDialog(stage);
		try {
			BufferedImage bImage = ImageIO.read(f);
			Image image = SwingFXUtils.toFXImage(bImage, null);
			gc.drawImage(image, 0, 0);
		} catch(IOException e) {
			System.out.println("Image draw error");
		}
	}
	
	/**
	 * Creates a png file of the current canvas
	 * 
	 * @param stage   current main stage
	 */
	public void export(Stage stage) {
		FileChooser choose = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter
				("png files (*.png)", "*.png");
		choose.getExtensionFilters().add(extFilter);
		Canvas canvas = page.getCanvas();
		int[] size = page.getSize();
		File f = choose.showSaveDialog(stage);
		try {
			WritableImage image = new WritableImage(size[0], size[1]);
			canvas.snapshot(null, image);
			RenderedImage rImage = SwingFXUtils.fromFXImage(image, null);
			ImageIO.write(rImage, "png", f);
		} 
		catch(IOException e) {
			System.out.println("IOException when exporting");
		}
	}
}
