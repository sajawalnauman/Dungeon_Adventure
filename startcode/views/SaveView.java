package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Class SaveView.
 *
 * Saves Serialized adventure games.
 */
public class SaveView implements Observer{

    static String saveFileSuccess = "Saved Adventure Game!!";
    static String saveFileExistsError = "Error: File already exists";
    static String saveFileNotSerError = "Error: File must end with .ser";
    private static Label saveFileErrorLabel = new Label("");
    private static Label saveGameLabel = new Label(String.format("Enter name of file to save"));
    private TextField saveFileNameTextField = new TextField("");
    private static Button saveGameButton = new Button("Save Game");
    private static Button closeWindowButton = new Button("Close Window");

    private static int currentFontSize = 16; //to keep font size information
    private static String currentFontStyle; //to keep font style information

    private AdventureGameView adventureGameView;

    /**
     * Constructor
     */
    public SaveView(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        saveGameLabel.setId("SaveGame"); // DO NOT MODIFY ID
        saveFileErrorLabel.setId("SaveFileErrorLabel");
        saveFileNameTextField.setId("SaveFileNameTextField");
        saveGameLabel.setStyle("-fx-text-fill: #e8e6e3;");
        saveGameLabel.setFont(new Font(currentFontSize));
        setFontStyleLabel(currentFontStyle, saveGameLabel);
        saveFileErrorLabel.setStyle("-fx-text-fill: #e8e6e3;");
        saveFileErrorLabel.setFont(new Font(currentFontSize));
        setFontStyleLabel(currentFontStyle, saveFileErrorLabel);
        saveFileNameTextField.setStyle("-fx-text-fill: #000000;");
        saveFileNameTextField.setFont(new Font(16));

        String gameName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".ser";
        saveFileNameTextField.setText(gameName);

        saveGameButton = new Button("Save board");
        saveGameButton.setId("SaveBoardButton"); // DO NOT MODIFY ID
        saveGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        saveGameButton.setPrefSize(200, 50);
        saveGameButton.setFont(new Font(currentFontSize));
        setFontStyleButton(currentFontStyle, saveGameButton);
        AdventureGameView.makeButtonAccessible(saveGameButton, "save game", "This is a button to save the game", "Use this button to save the current game.");
        saveGameButton.setOnAction(e -> saveGame());

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(currentFontSize));
        setFontStyleButton(currentFontStyle, closeWindowButton);
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the save game window", "Use this button to close the save game window.");

        VBox saveGameBox = new VBox(10, saveGameLabel, saveFileNameTextField, saveGameButton, saveFileErrorLabel, closeWindowButton);
        saveGameBox.setAlignment(Pos.CENTER);

        dialogVbox.getChildren().add(saveGameBox);
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
        labels.add(saveFileErrorLabel);
        labels.add(saveGameLabel);
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
        buttons.add(saveGameButton);
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
     * Saves the Game
     * Save the game to a serialized (binary) file.
     * Get the name of the file from saveFileNameTextField.
     * Files will be saved to the Games/Saved directory.
     * If the file already exists, set the saveFileErrorLabel to the text in saveFileExistsError
     * If the file doesn't end in .ser, set the saveFileErrorLabel to the text in saveFileNotSerError
     * Otherwise, load the file and set the saveFileErrorLabel to the text in saveFileSuccess
     */
    private void saveGame() {

        File theDir = new File("Games/Saved/");
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        File file = new File("Games/Saved/" + saveFileNameTextField.getText());

        boolean exists = file.exists();

        if (exists) {
            this.saveFileErrorLabel.setText(saveFileExistsError);
        } else if (!(saveFileNameTextField.getText().contains(".ser"))) {
            saveFileErrorLabel.setText(saveFileNotSerError);
        }
        else {
            this.adventureGameView.model.saveModel(file);
            saveFileErrorLabel.setText(saveFileSuccess);
        }
    }
}
