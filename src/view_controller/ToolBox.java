package view_controller;

import java.io.File;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Eraser;
import model.FillTool;
import model.RectSelect;
import model.TextTool;
import model.Brush;

/**
 * Class ToolBox as an HBox that stays on top of the page. Contains different
 * types of tools for the user.
 */
public class ToolBox extends HBox {
	private Image image; // Image tool for image manipulation
	private Shapes shapes; // Shapes tool for drawing shapes
	private InnerPage innerPage; // The main drawing area
	private ComboBox<Brush.BrushType> brushTypeComboBox; // ComboBox for selecting brush type
	private ComboBox<Integer> brushSizeComboBox; // ComboBox for selecting brush size
	private FillTool fillTool; // The fill tool
	private RectSelect rectSelect; // The rectangular select tool
	private ColorPicker colorPicker;
	private TextTool text;
	private int textSize = 10;
	private ComboBox<String> shapeComboBox;
	private Image eraserIcon;
	private Image brushIcon;
	private Image fillIcon;
	private Image textIcon;
	private Image deleteIcon;
	private Image moveAndCopyIcon;
	private Image redoIcon;
	private Image undoIcon;
	private Image imageIcon;

	/**
	 * Constructor for the class
	 */
	public ToolBox() {
		layOut();
	}

	/**
	 * Helper method for constructor. Lay out every tool in the Tool Box
	 */
	private void layOut() {
		shapes = new Shapes(null, null);
		fillTool = new FillTool(Color.BLACK); // Initialize the fill tool
		rectSelect = new RectSelect(); // Initialize the rectangular select tool
		eraserIcon = new Image("file:imagesIcon/eraser2.png");
		brushIcon = new Image("file:imagesIcon/brush.png");
		fillIcon = new Image("file:imagesIcon/fill.png");
		textIcon = new Image("file:imagesIcon/text.png");
		deleteIcon = new Image("file:imagesIcon/delete.png");
		moveAndCopyIcon = new Image("file:imagesIcon/moveAndcopy.png");
		redoIcon = new Image("file:imagesIcon/redo.png");
		undoIcon = new Image("file:imagesIcon/undo.png");
		imageIcon = new Image("file:imagesIcon/imageIcon.png");

		Eraser eraser = new Eraser(10); // Create an eraser with size 10
		text = new TextTool(textSize);
		Slider eraserSizeSlider = eraser.createEraserSizeSlider(eraser); // Create a slider to adjust the eraser size

		Brush brush = new Brush(1, Brush.BrushType.ROUND); // Create a brush with size 1 and type ROUND
		// Create a drop-down menu for selecting the brush type, it has 5 options
		brushTypeComboBox = brush.createBrushTypeComboBox();

		// Create the main drawing area with the eraser and brush tools
		innerPage = new InnerPage(eraser, brush, text, fillTool, rectSelect);
		innerPage.setMaxWidth(950); // Set maximum width and height for InnerPage
		innerPage.setMaxHeight(500);

		// Create a color picker for the user to choose a color
		ColorPicker colorPicker = createColorPicker(innerPage, brush, fillTool);

		// Create a drop-down menu for selecting the brush type
		brushTypeComboBox = brush.createBrushTypeComboBox(); // brush type
		brushSizeComboBox = brush.createBrushSizeComboBox(); // brush size

		// Create all button for the tool box
		Button deleteButton = createButton("Delete", "DELETE", innerPage, deleteIcon);
		Button moveCopyButton = createButton("Move/Copy", "MOVECOPY", innerPage, moveAndCopyIcon);
		Button fillButton = createButton("Fill", "FILL", innerPage, fillIcon);
		Button brushButton = createButton("Brush", "BRUSH", innerPage, brushIcon);
		Button eraserButton = createButton("Eraser", "ERASER", innerPage, eraserIcon);
		Button textButton = createButton("TextTool", "TEXT", innerPage, textIcon);
		Button textIncreaseButton = createButton("+", "+", innerPage, null);
		Button textDecreaseButton = createButton("-", "-", innerPage, null);
		// Create Undo and Redo buttons
		Button undoButton = createButton("Undo", "UNDO", innerPage, undoIcon);
		Button redoButton = createButton("Redo", "REDO", innerPage, redoIcon);

		// add icon to the load image button
		Button LoadImg = Main.getLoadImageButton();
		ImageView view = new ImageView(imageIcon);
		view.setFitHeight(20);
		view.setPreserveRatio(true);
		LoadImg.setGraphic(view);
		LoadImg.setStyle("-fx-base: #93c572; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;");
		Button RemoveImg = Main.getRemoveImageButton();

		// create combo box for the shapes
		shapeComboBox = new ComboBox<>();
		shapeComboBox.getItems().addAll("Line", "Curve", "Square", "Triangle", "Circle", "Rectangle");
		shapeComboBox.setValue("Select a shape");
		shapeComboBox.setOnAction(event -> {
			String selectedShape = shapeComboBox.getValue();
			switch (selectedShape) {
			case "Line":
				innerPage.setTool("LINE");
				break;
			case "Square":
				innerPage.setTool("SQUARE");
				break;
			case "Triangle":
				innerPage.setTool("TRIANGLE");
				break;
			case "Circle":
				innerPage.setTool("CIRCLE");
				break;
			case "Rectangle":
				innerPage.setTool("RECTANGLE");
				break;
			case "Curve":
				innerPage.setTool("CURVE");
				break;
			}
		});

		// Create a VBox for the brush button and ComboBoxes
		VBox brushBox = new VBox(brushButton, brushTypeComboBox, brushSizeComboBox);

		// Create a VBox for the eraser button and its related components
		VBox eraserBox = new VBox(eraserButton, eraserSizeSlider);

		// For color box only
		VBox colorBox = new VBox(colorPicker);

		// For fillTool only
		VBox fillBox = new VBox(fillButton);

		// For rectSelect Tool!
		VBox rectSelectBox = new VBox();
		rectSelectBox.getChildren().addAll(deleteButton, moveCopyButton);
		rectSelectBox.setSpacing(5); // Set the gap between buttons (adjust the value as needed)

		VBox shapeBox = new VBox(shapeComboBox);
		VBox loadBox = new VBox(LoadImg, RemoveImg);
		loadBox.setSpacing(5);

		HBox undoAndredoBox = new HBox(undoButton, redoButton);
		undoAndredoBox.setSpacing(5);

		// Create a HBox for increase and decrease text
		// and add them together with the textButton
		GridPane textPane = new GridPane();
		double preferredWidth = 23;
		textIncreaseButton.setPrefWidth(preferredWidth);
		textDecreaseButton.setPrefWidth(preferredWidth);
		HBox textBox = new HBox(10, textIncreaseButton, textDecreaseButton);
		textPane.add(textButton, 0, 0);
		textPane.add(textBox, 0, 1);
		textPane.setVgap(5);
		// Set the vertical alignment for the textButton to center
		GridPane.setValignment(textButton, VPos.CENTER);
		// Set the horizontal alignment for the textButton to center
		GridPane.setHalignment(textButton, HPos.CENTER);

		// Create an HBox for all tools
		HBox tools = new HBox(10, rectSelectBox, brushBox, eraserBox, colorBox, textPane, shapeBox, loadBox, fillBox,
				undoAndredoBox);
		tools.setAlignment(Pos.CENTER); // Align all buttons to the center
		tools.setPadding(new Insets(15, 0, 0, 0));

		// Create a VBox for the tools area
		VBox toolsArea = new VBox(tools);
		toolsArea.setSpacing(20);
		toolsArea.setPadding(new Insets(5, 10, 0, 50));

		// Add the VBox to this HBox
		this.getChildren().addAll(toolsArea);
		this.setSpacing(35);
		BorderStroke borderStroke = new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				new BorderWidths(1.5));
		// Set the new border to this
		this.setBorder(new Border(borderStroke));
	}

	/**
	 * Creates a button with the specified text, tool type, and related UI elements.
	 * Sets up the button's behavior and appearance. If the tool type is "+" or "-",
	 * the method creates a button with text. For other tool types, it creates a
	 * button with an icon.
	 *
	 * @param text      The text displayed on the button. This is used only if the
	 *                  tool type is "+" or "-".
	 * @param tool      The type of tool associated with the button. This can be any
	 *                  tool type such as "BRUSH", "ERASER", "FILL", "+", or "-".
	 * @param innerPage The InnerPage object representing the drawing area. This is
	 *                  where the tool will be used when the button is clicked.
	 * @param icon      The icon displayed on the button. This is used only if the
	 *                  tool type is not "+" or "-".
	 * 
	 * @return The created button.
	 */
	private Button createButton(String text, String tool, InnerPage innerPage, Image icon) {
		ImageView imageView = new ImageView(icon);

		if (tool.equals("-") || tool.equals("+")) {
			// Create a new button with the specified text
			Button button = new Button(text);
			// Set an action event for the button
			button.setOnAction(e -> {
				innerPage.setTool(tool);
				// Check if the selected tool is TEXT
				if (tool.equals("+")) {
					textSize += 5;
					this.text.setSize(textSize);
					innerPage.setTool("TEXT");
				} else if (tool.equals("-")) {
					textSize -= 5;
					this.text.setSize(textSize);
					innerPage.setTool("TEXT");
				} else if (tool.equals("FILL")) {
					fillTool.setColor(colorPicker.getValue());
				}

				// Check if setting the tool affects the behavior
				System.out.println("Current tool: " + innerPage.getTool());
			});
			button.setStyle("-fx-base: #ccdfc4; -fx-text-fill: black; -fx-font-size: 12px; -fx-padding: 4;");

			return button;
		}
		// Create a new button with the specified icon
		Button button = new Button("", imageView);

		// Set an action event for the button
		button.setOnAction(e -> {
			innerPage.setTool(tool);
			if (tool.equals("UNDO")) {
				innerPage.undo();
			} else if (tool.equals("REDO")) {
				innerPage.redo();
			}
			// Check if the selected tool is FILL
			if (tool.equals("FILL")) {
				fillTool.setColor(colorPicker.getValue());
			}
		});
		// Set the style for the button
		button.setStyle("-fx-base: #93c572; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;");

		// Return the created button
		return button;
	}

	/**
	 * Creates and configures a ColorPicker for selecting drawing colors. Sets up
	 * behavior for color selection.
	 *
	 * @param innerPage The InnerPage object representing the drawing area
	 * @param brush     The Brush object representing the drawing tool
	 * @param fillTool  The FillTool object representing the fill colors
	 * 
	 * @return The configured ColorPicker
	 */
	private ColorPicker createColorPicker(InnerPage innerPage, Brush brush, FillTool fillTool) {
		// Create a new color picker
		colorPicker = new ColorPicker();

		// Set the initial color value of the color picker to black
		colorPicker.setValue(Color.BLACK);

		// Set an action event for the color picker
		colorPicker.setOnAction(e -> {
			// Set the current color in InnerPage when a color is picked
			innerPage.setColor(colorPicker.getValue());
			brush.setColor(colorPicker.getValue());
			fillTool.setColor(colorPicker.getValue());

		});

		// Return the created color picker
		return colorPicker;
	}

	/**
	 * Get inner page/canvas for Main class
	 * 
	 * @return Inner page
	 */
	public InnerPage getInnerPage() {
		return innerPage;
	}

}
