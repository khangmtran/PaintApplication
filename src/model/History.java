package model;

import javafx.scene.image.WritableImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class manages the undo and redo operations in an application.
 */
public class History {
    // A list to store the snapshots of the application at different points in time
    private List<WritableImage> states;
    // An index to keep track of the current snapshot in the list
    private int currentStateIndex;
    private boolean isRedoInProgress; // Added flag


    /**
     * Constructor for the History class.
     * Initializes the states list and sets the current state index to -1.
     */
    public History() {
        states = new ArrayList<>();
        currentStateIndex = -1;
    }

    /**
     * Adds a new snapshot to the list.
     * If the user performed an action after undo, it discards the redo snapshots.
     * @param state The new snapshot to be added.
     */
    public void addState(WritableImage state) {
        if (currentStateIndex < states.size() - 1) {
            states.subList(currentStateIndex + 1, states.size()).clear();
        }
        states.add(state);
        currentStateIndex++;
    }

    /**
     * Performs the undo operation by decrementing the current state index.
     */
    public void undo() {
        if (currentStateIndex > 0) {
            currentStateIndex--;
        }
    }

    /**
     * Performs the redo operation by incrementing the current state index.
     * It also adds the redo snapshot as a new snapshot to the list.
     */
    public void redo() {
        if (currentStateIndex < states.size() - 1) {
            currentStateIndex++;
        }
    }

    /**
     * Returns the current snapshot of the application.
     * @return The current snapshot if it exists, null otherwise.
     */
    public WritableImage getCurrentState() {
        if (currentStateIndex >= 0 && currentStateIndex < states.size()) {
            return states.get(currentStateIndex);
        }
        return null;
    }

    /**
     * Returns a copy of the snapshots list.
     * @return A new list containing all the snapshots.
     */
    public List<WritableImage> getStates() {
        return new ArrayList<>(states);
    }

    /**
     * Adds a new action to the list.
     * If the user performed an action after undo, it discards the redo snapshots.
     * @param snapshot The snapshot of the new action.
     */
    public void addAction(WritableImage snapshot) {
        if (currentStateIndex < states.size() - 1) {
            states.subList(currentStateIndex + 1, states.size()).clear();
        }
        states.add(snapshot);
        currentStateIndex++;
    }

    /**
     * Checks if the list of snapshots is empty.
     * @return true if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return states.isEmpty();
    }
}

