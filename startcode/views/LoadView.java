package views;

import AdventureModel.AdventureGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Class LoadView.
 *
 * Loads Serialized adventure games.
 */
public class LoadView implements Observer {

    private AdventureGameView adventureGameView;
    private static Label selectGameLabel = new Label(String.format(""));
    private static Button selectGameButton = new Button("Change Game");
    private static Button closeWindowButton = new Button("Close Window");
    private static Label historyLabel = new Label(String.format("")); //to hold history information

    private static int currentFontSize = 16; //to keep font size information
    private static String currentFontStyle; //to keep font style information

    private ListView<String> GameList;

    public LoadView(AdventureGameView adventureGameView){

        //note that the buttons in this view are not accessible!!
        this.adventureGameView = adventureGameView;

        GameList = new ListView<>(); //to hold all the file names

        final Stage dialog = new Stage(); //dialogue box
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        selectGameLabel.setId("CurrentGame"); // DO NOT MODIFY ID
        historyLabel.setId("Game History");
        GameList.setId("GameList");  // DO NOT MODIFY ID
        GameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        getFiles(GameList); //get files for file selector
        selectGameButton.setId("ChangeGame"); // DO NOT MODIFY ID
        AdventureGameView.makeButtonAccessible(selectGameButton, "select game", "This is the button to select a game", "Use this button to indicate a game file you would like to load.");

        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(currentFontSize));
        setFontStyleButton(currentFontStyle, closeWindowButton);
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the load game window", "Use this button to close the load game window.");

        //on selection, do something
        selectGameButton.setOnAction(e -> {
            try {
                selectGame(selectGameLabel, GameList);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        GameList.setOnMouseClicked(event -> {
            try {
                viewHistory(GameList, historyLabel);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");

        VBox selectGameBox = new VBox();
        selectGameBox.getChildren().addAll(selectGameLabel, GameList, historyLabel, selectGameButton);
        selectGameBox.setSpacing(10);

        // Default styles which can be modified
        GameList.setPrefHeight(100);
        selectGameLabel.setStyle("-fx-text-fill: #e8e6e3");
        selectGameLabel.setFont(new Font(currentFontSize));
        setFontStyleLabel(currentFontStyle, selectGameLabel);
        historyLabel.setStyle("-fx-text-fill: #e8e6e3");
        historyLabel.setFont(new Font(currentFontSize));
        setFontStyleLabel(currentFontStyle, historyLabel);
        selectGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        selectGameButton.setPrefSize(200, 50);
        selectGameButton.setFont(new Font(currentFontSize));
        setFontStyleButton(currentFontStyle, selectGameButton);
        selectGameBox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(selectGameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Retrieves a list containing all the Label objects associated with the current class.
     * The labels include room description, object description, inventory details, and command information.
     *
     * @return A List of Label objects representing various informational elements in the class.
     */
    private static List<Label> getAllLabels() {
        List<Label> labels = new ArrayList<>();
        labels.add(selectGameLabel);
        labels.add(historyLabel);
        return labels;
    }

    /**
     * Retrieves a list containing all the Button objects associated with the current class.
     * The buttons include functionality for saving, loading, accessing help, and adjusting settings.
     *
     * @return A List of Button objects representing various actions in the class.
     */
    private static List<Button> getAllButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(selectGameButton);
        buttons.add(closeWindowButton);
        return buttons;
    }

    /**
     * Updates the observer with new size information.
     *
     * @param newSize The new size information received from the subject.
     */
    @Override
    public void updateSize(String newSize) {
        updateFontSize(newSize);
    }

    /**
     * Updates the font size of all labels and buttons in the current class.
     * The font size can be set to "Big" (size 24) or the default size "Normal" (size 16).
     *
     * @param newSize The desired font size, either "Big" or "Normal".
     */
    public static void updateFontSize(String newSize) {
        if (Objects.equals(newSize, "Big")) {
            for (Label label : getAllLabels()) {
                label.setFont(new Font(24));
            }
            for (Button button : getAllButtons()) {
                button.setFont(new Font(24));
            }
            currentFontSize = 24;
        } else {
            for (Label label : getAllLabels()) {
                label.setFont(new Font(16));
            }
            for (Button button : getAllButtons()) {
                button.setFont(new Font(16));
            }
            currentFontSize = 16;
        }

    }

    /**
     * Updates the observer with new style information.
     *
     * @param newStyle The new style information received from the subject.
     */
    @Override
    public void updateStyle(String newStyle) {
        updateFontStyle(newStyle);
    }

    /**
     * Updates the font style of all labels and buttons in the current class.
     * The font style can be set to "Bold" or the default style "Normal".
     *
     * @param newStyle The desired font style, either "Bold" or "Normal".
     */
    public static void updateFontStyle(String newStyle) {
        if (Objects.equals(newStyle, "Bold")) {
            for (Label label : getAllLabels()) {
                label.setFont(Font.font(label.getFont().getFamily(), FontWeight.BOLD, label.getFont().getSize()));
            }
            for (Button button : getAllButtons()) {
                button.setFont(Font.font(button.getFont().getFamily(),FontWeight.BOLD, button.getFont().getSize()));
            }
            currentFontStyle = "Bold";
        } else {
            for (Label label : getAllLabels()) {
                label.setFont(Font.font(label.getFont().getFamily(),FontWeight.NORMAL, label.getFont().getSize()));
            }
            for (Button button : getAllButtons()) {
                button.setFont(Font.font(button.getFont().getFamily(),FontWeight.NORMAL, button.getFont().getSize()));
            }
            currentFontStyle = "Regular";
        }
    }

    /**
     * Sets the font style for a JavaFX Label.
     *
     * @param style  The desired font style, either "Bold" or "Normal".
     * @param label  The JavaFX Label for which the font style needs to be set.
     */
    public static void setFontStyleLabel(String style, Label label) {
        if (Objects.equals(style, "Bold")) {
            label.setFont(Font.font(label.getFont().getFamily(), FontWeight.BOLD, label.getFont().getSize()));
        }
        else {
            label.setFont(Font.font(label.getFont().getFamily(), FontWeight.NORMAL, label.getFont().getSize()));
        }
    }

    /**
     * Sets the font style for a JavaFX Button.
     *
     * @param style   The desired font style, either "Bold" or "Normal".
     * @param button  The JavaFX Button for which the font style needs to be set.
     */
    public static void setFontStyleButton(String style, Button button) {
        if (Objects.equals(style, "Bold")) {
            button.setFont(Font.font(button.getFont().getFamily(), FontWeight.BOLD, button.getFont().getSize()));
        }
        else {
            button.setFont(Font.font(button.getFont().getFamily(), FontWeight.NORMAL, button.getFont().getSize()));
        }
    }

    /**
     * Get Files to display in the on screen ListView
     * Populate the listView attribute with .ser file names
     * Files will be located in the Games/Saved directory
     *
     * @param listView the ListView containing all the .ser files in the Games/Saved directory.
     */
    private void getFiles(ListView<String> listView) {
        File dir = new File("Games/Saved/");
        File[] directoryListing = dir.listFiles();

        assert directoryListing != null;
        for (File file : directoryListing) {
            if (file.getName().contains(".ser")) {
                listView.getItems().add(file.getName());
            }
        }
    }

    /**
     * Select the Game
     * Try to load a game from the Games/Saved
     * If successful, stop any articulation and put the name of the loaded file in the selectGameLabel.
     * If unsuccessful, stop any articulation and start an entirely new game from scratch.
     * In this case, change the selectGameLabel to indicate a new game has been loaded.
     *
     * @param selectGameLabel the label to use to print errors and or successes to the user.
     * @param GameList the ListView to populate
     */
    private void selectGame(Label selectGameLabel, ListView<String> GameList) throws IOException {
        //saved games will be in the Games/Saved folder!
        this.adventureGameView.stopArticulation();

        try {
            String name = GameList.getSelectionModel().getSelectedItem();
            AdventureGame game = this.loadGame("Games/Saved/" + name);

            if (game != null) {
                this.adventureGameView.model = game;
                selectGameLabel.setText(name);
                if (this.adventureGameView.model.timerState.description().equals("ResumeTimerState")) {
                    this.adventureGameView.startTimer();
                    this.adventureGameView.model.gameTimer.startTimer();
                    this.adventureGameView.inputTextField.setDisable(false);
                    this.adventureGameView.objectsInRoom.setDisable(false);
                    this.adventureGameView.objectsInInventory.setDisable(false);
                } else {
                    this.adventureGameView.model.gameTimer.stopTimer();
                    this.adventureGameView.pauseResumeButton.fire();
                }
            }
        }

        catch (FileNotFoundException | ClassNotFoundException e) {
            selectGameLabel.setText("New Game Loaded");
            AdventureGame game = new AdventureGame("TinyGame");
            this.adventureGameView.model = game;
        }

        this.adventureGameView.updateItems();
        this.adventureGameView.updateScene(this.adventureGameView.model.player.getCurrentRoom().getRoomDescription());

    }

    /**
     * Load the Game from a file
     *
     * @param GameFile file to load
     * @return loaded Tetris Model
     */
    public AdventureGame loadGame(String GameFile) throws IOException, ClassNotFoundException {
        // Reading the object from a file
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream(GameFile);
            in = new ObjectInputStream(file);
            return (AdventureGame) in.readObject();
        } finally {
            if (in != null) {
                in.close();
                file.close();
            }
        }
    }

    /**
     * Select the Game
     * Set the texts of the historyLabel as the room name and inventory of the selected game
     * @param GameList the ListView to populate
     * @param historyLabel label that stores history information
     */
    public void viewHistory(ListView<String> GameList, Label historyLabel) throws IOException, ClassNotFoundException {
        try {
            String name = GameList.getSelectionModel().getSelectedItem();
            AdventureGame game = this.loadGame("Games/Saved/" + name);
            String room_name = game.getPlayer().getCurrentRoom().getRoomName();
            ArrayList<String> objStr = new ArrayList<>(game.getPlayer().getInventory());
            String inventory = String.join(", ", objStr);

            String historyText = "Current Room: " + room_name + "\nInventory: " + inventory;
            historyLabel.setText(historyText);

        } catch (FileNotFoundException | ClassNotFoundException e) {
            historyLabel.setText("");
        }

    }

}
