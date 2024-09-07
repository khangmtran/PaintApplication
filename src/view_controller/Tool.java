package view_controller;

/**
 * Interface represents a tool in the application that has a size attribute.
 */
public interface Tool {
	/**
	 * set size for the tool where size represents how big an object will be placed
	 * on the canvas
	 * 
	 * @param size
	 */
	void setSize(int size);

	/**
	 * Get the size of the current tool
	 * 
	 * @return size
	 */
	int getSize();
}
