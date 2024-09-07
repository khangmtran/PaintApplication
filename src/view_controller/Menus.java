package view_controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import model.file;
import view_controller.InnerPage;

/**
 * Menu contains file menu at the top of the application to let user manipulate
 * the page they are working on
 */
public class Menus {
	private MenuItem newPage = new MenuItem("New Page");
	private MenuItem save = new MenuItem("Save");
	private MenuItem open = new MenuItem("Open file");
	private MenuItem export = new MenuItem("Export Image");
	private Menu file = new Menu("File");
	private MenuBar menuBar = new MenuBar();
	private file fl;
	private Stage primaryStage;
	private InnerPage canvas;

	/**
	 * Constructor
	 * 
	 * @param f      Menu's name
	 * @param stage  Current main stage
	 * @param canvas The canvas being represented
	 */
	public Menus(file f, Stage stage, InnerPage canvas) {
		this.canvas = canvas;
		fl = f;
		primaryStage = stage;
		file.getItems().addAll(newPage, save, open, export);
		menuBar.getMenus().addAll(file);
		registerListener();
	}

	/**
	 * Add register to the menu items
	 */
	private void registerListener() {
		newPage.setOnAction(new MenuListener());
		save.setOnAction(new MenuListener());
		open.setOnAction(new MenuListener());
		export.setOnAction(new MenuListener());
	}

	/**
	 * Menulistener class handles the actions
	 */
	public class MenuListener implements EventHandler<ActionEvent> {

		/**
		 * Handles the actions of the menu item
		 * @param arg0 Argument represents for the source
		 */
		@Override
		public void handle(ActionEvent arg0) {
			MenuItem menuClicked = (MenuItem) arg0.getSource();
			// Create a new canvas
			if (newPage == menuClicked) {
				canvas.newPage();
			} else if (save == menuClicked) {
				fl.export(primaryStage);
			} else if (open == menuClicked) {
				fl.drawImage(primaryStage);
			} else if (export == menuClicked) {
				fl.export(primaryStage);
			}
		}
	}

	/**
	 * The main menu bar contains menu
	 * @return menu bar
	 */
	public MenuBar getMenu() {
		return menuBar;
	}
}
