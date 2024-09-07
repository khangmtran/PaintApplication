package view_controller;

import javafx.geometry.Insets;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.file;

/**
 * The Main class extends the Application class from JavaFX and represents the
 * main application window.
 * 
 * @author Alex, Mary, Khang, Yen
 */
public class Main extends Application {
	private Menus menu;// Menu contains file/view on top of the application
	// Used as a wrapper to position the canvas centrally at the bottom of the
	// BorderPane.
	private HBox bottomBox;
	// Used for zoomIn/Out
	private Slider zoomSlider;

	private Group imageGroup;
	private double startX;
	private double startY;
	private static Button loadImageBtn = new Button();
	private static Button removeImageBtn = new Button("Remove Image");

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Handling the working application
	 * 
	 * @param primaryStage The main stage represents the application
	 */
	public void start(Stage primaryStage) {
		// Initialize the menu and tool box
		ToolBox toolBox = new ToolBox();

		// Create a new file object to save the file to the computer
		file f = new file(toolBox.getInnerPage());
		menu = new Menus(f, primaryStage, toolBox.getInnerPage());

		// Call InnerPage and use its method to display the zoom onto the canvas itself
		zoomSlider = toolBox.getInnerPage().zoomBySlider(toolBox.getInnerPage());
		VBox drawingArea = createDrawingArea(toolBox.getInnerPage());
		InnerPage drawingArea2 = toolBox.getInnerPage();
		// Create a new BorderPane. This will be the main view where the user can see
		// the menu, tool-box, and drawing area
		BorderPane pane = new BorderPane();

		// Set the tool box to be in the center of the pane
		pane.setCenter(toolBox);

		// Create a drawing area and add it to an HBox

		pane.setTop(menu.getMenu()); // Set the menu to be at the top of the pane
		bottomBox = new HBox();
		bottomBox.setAlignment(Pos.CENTER);

		bottomBox.setPadding(new Insets(10, 10, 10, 0));
		bottomBox.setStyle("-fx-background-color: #E6E6FA;"); // This sets the background to a light purple
		bottomBox.getChildren().add(drawingArea);
		drawingArea.getChildren().add(zoomSlider);
		pane.setBottom(bottomBox); // Set the HBox to be at the bottom of the pane

		// used for user importing an image of their choice
		loadImageBtn.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Image File");
			fileChooser.getExtensionFilters()
					.addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);

			if (selectedFile != null) {
				Image image = new Image("file:" + selectedFile.getAbsolutePath());
				ImageView imageView = new ImageView(image);
				imageView.setPreserveRatio(true);
				imageView.setFitHeight(400); // Set initial size
				imageView.setFitWidth(400);

				Group handler = makeHandler();
				handler.translateXProperty().bindBidirectional(imageView.fitWidthProperty());
				handler.translateYProperty().bindBidirectional(imageView.fitHeightProperty());

				this.imageGroup = new Group(imageView, handler);
				drawingArea2.getChildren().add(this.imageGroup); // Add imageGroup to your drawing area
			}
		});

		// used for removing user chosen image
		removeImageBtn.setOnAction(e -> {
			// Check if the imageGroup has been added and remove it
			drawingArea2.getChildren().remove(this.imageGroup);
			// drawingArea.getChildren().remove(this.imageGroup);
			imageGroup = null; // Set imageGroup to null after removal

		});

		// Set up the scene and show the primaryStage
		primaryStage.setTitle("Paint Program");
		primaryStage.setScene(new Scene(pane, 980, 700));
		primaryStage.show();
	}

	/**
	 * Creates the symbol on the image that lets the user drag and resize it.
	 * 
	 * @param None
	 * @return the symbol attached to the image
	 */
	private Group makeHandler() {
		Polygon polygon = new Polygon();
		Group group = new Group(polygon);
		polygon.getPoints().addAll(0.0, 0.0, 0.0, -20.0, -20.0, 0.0);
		polygon.setStroke(Color.BLACK);
		polygon.setFill(Color.AZURE);
		polygon.setStrokeWidth(2);
		polygon.setStrokeType(StrokeType.INSIDE);

		group.setOnMousePressed(e -> {
			startX = group.getLayoutX() - e.getX();
			startY = group.getLayoutY() - e.getY();
		});

		group.setOnMouseDragged(e -> {
			group.setTranslateX(group.getTranslateX() + e.getX() + startX);
			group.setTranslateY(group.getTranslateY() + e.getY() + startY);
		});

		return group;
	}

	/**
	 * Creates a VBox containing an InnerPage object. This VBox represents a drawing
	 * area.
	 * 
	 * @param innerPage An InnerPage object representing a draw-able area in a GUI.
	 * @return A VBox containing an InnerPage object.
	 */
	private VBox createDrawingArea(InnerPage innerPage) {
		// Create a new VBox and add InnerPage to it
		VBox vBox = new VBox(innerPage);

		// Center InnerPage vertically within the VBox
		vBox.setAlignment(Pos.CENTER);

		// Return the created VBox
		return vBox;
	}

	public static Button getLoadImageButton() {
		return loadImageBtn;
	}

	public static Button getRemoveImageButton() {
		return removeImageBtn;
	}

}
