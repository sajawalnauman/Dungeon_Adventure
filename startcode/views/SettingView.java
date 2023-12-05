package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import views.AdventureGameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The SettingView class represents the settings window in the adventure game.
 * Users can adjust font size and style preferences using this window.
 */
public class SettingView implements Subject{
    private List<Observer> observers = new ArrayList<>(); //list of observers: AdventureGameView, SaveView, LoadView

    private static Label settingLabel = new Label(String.format("Select font size and style you like."));
    private static Label fontsizeLabel = new Label(String.format(""));
    private static Label fontstyleLabel = new Label(String.format(""));
    public static Button bigSizeButton, regularSizeButton;
    public static Button boldStyleButton, regularStyleButton;
    private static Button closeWindowButton;
    private static int currentFontSize = 16; //to keep font size information
    private static String currentFontStyle; //to keep font style information

    /**
     * Constructs a SettingView associated with the provided AdventureGameView.
     *
     * @param adventureGameView The AdventureGameView instance associated with this SettingView.
     */
    public SettingView(AdventureGameView adventureGameView, SaveView saveView, LoadView loadView) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        settingLabel.setId("SaveGame"); // DO NOT MODIFY ID
        fontsizeLabel.setId("fontSizeLabel");
        fontstyleLabel.setId("fontStyleLabel");
        settingLabel.setStyle("-fx-text-fill: #e8e6e3;");
        settingLabel.setFont(new Font(currentFontSize));
        setFontStyleLabel(currentFontStyle, settingLabel);
        fontsizeLabel.setStyle("-fx-text-fill: #e8e6e3;");
        fontsizeLabel.setFont(new Font(currentFontSize));
        setFontStyleLabel(currentFontStyle, fontsizeLabel);
        fontstyleLabel.setStyle("-fx-text-fill: #e8e6e3;");
        fontstyleLabel.setFont(new Font(currentFontSize));
        setFontStyleLabel(currentFontStyle, fontstyleLabel);

        // register observers
        addObserver(adventureGameView);
        addObserver(saveView);
        addObserver(loadView);

        bigSizeButton = new Button("Big Size");
        bigSizeButton.setId("bigSizeButton"); // DO NOT MODIFY ID
        bigSizeButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        bigSizeButton.setPrefSize(200, 50);
        bigSizeButton.setFont(new Font(currentFontSize));
        setFontStyleButton(currentFontStyle, bigSizeButton);
        AdventureGameView.makeButtonAccessible(bigSizeButton, "big size button", "This is a button to set the font size big.", "Use this button to make the font size bigger.");
        bigSizeButton.setOnAction(e -> {
            notifyObservers_Size("Big");
            updateFontSize("Big");
            fontsizeLabel.setText("Font size selected: Big");
        });

        regularSizeButton = new Button("Regular Size");
        regularSizeButton.setId("regularSizeButton"); // DO NOT MODIFY ID
        regularSizeButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        regularSizeButton.setPrefSize(200, 50);
        regularSizeButton.setFont(new Font(currentFontSize));
        setFontStyleButton(currentFontStyle, regularSizeButton);
        AdventureGameView.makeButtonAccessible(regularSizeButton, "regular size button", "This is a button to set the font size regular.", "Use this button to make the font size regular.");
        regularSizeButton.setOnAction(e -> {
            notifyObservers_Size("Regular");
            updateFontSize("Regular");
            fontsizeLabel.setText("Font size selected: Regular");
        });

        regularStyleButton = new Button("Regular Style");
        regularStyleButton.setId("regularStyleButton"); // DO NOT MODIFY ID
        regularStyleButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        regularStyleButton.setPrefSize(200, 50);
        regularStyleButton.setFont(new Font(currentFontSize));
        setFontStyleButton(currentFontStyle, regularStyleButton);
        AdventureGameView.makeButtonAccessible(regularStyleButton, "big size button", "This is a button to set the font size big.", "Use this button to make the font size bigger.");
        regularStyleButton.setOnAction(e -> {
            notifyObservers_Style("Regular");
            updateFontStyle("Regular");
            fontstyleLabel.setText("Font style selected: Regular");
        });

        boldStyleButton = new Button("Bold Style");
        boldStyleButton.setId("bigSizeButton"); // DO NOT MODIFY ID
        boldStyleButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        boldStyleButton.setPrefSize(200, 50);
        boldStyleButton.setFont(new Font(currentFontSize));
        setFontStyleButton(currentFontStyle, boldStyleButton);
        AdventureGameView.makeButtonAccessible(boldStyleButton, "big size button", "This is a button to set the font size big.", "Use this button to make the font size bigger.");
        boldStyleButton.setOnAction(e -> {
            notifyObservers_Style("Bold");
            updateFontStyle("Bold");
            fontstyleLabel.setText("Font style selected: Bold");
        });

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(currentFontSize));
        setFontStyleButton(currentFontStyle, closeWindowButton);
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the save game window", "Use this button to close the save game window.");

        HBox fontSizeButtonBox = new HBox(20, bigSizeButton, regularSizeButton);
        HBox fontStyleButtonBox = new HBox(20, regularStyleButton, boldStyleButton);

        VBox settingBox = new VBox(10, settingLabel, fontSizeButtonBox, fontsizeLabel, fontStyleButtonBox, fontstyleLabel, closeWindowButton);
        settingBox.setAlignment(Pos.CENTER);

        dialogVbox.getChildren().add(settingBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Adds an observer to the list of observers for this subject.
     *
     * @param observer The observer to be added.
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Notifies all registered observers about a change in size.
     *
     * @param newSize The new size information to be communicated to observers.
     */
    @Override
    public void notifyObservers_Size(String newSize) {
        for (Observer observer : observers) {
            if (observer != null) {
                observer.updateSize(newSize);
            }
        }
    }

    /**
     * Notifies all registered observers about a change in style.
     *
     * @param newStyle The new style information to be communicated to observers.
     */
    @Override
    public void notifyObservers_Style(String newStyle) {
        for (Observer observer : observers) {
            if (observer != null) {
                observer.updateStyle(newStyle);
            }
        }
    }

    /**
     * Retrieves a list containing all the Label objects associated with the current class.
     * The labels include room description, object description, inventory details, and command information.
     *
     * @return A List of Label objects representing various informational elements in the class.
     */
    private static List<Label> getAllLabels() {
        List<Label> labels = new ArrayList<>();
        labels.add(settingLabel);
        labels.add(fontsizeLabel);
        labels.add(fontstyleLabel);
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
        buttons.add(bigSizeButton);
        buttons.add(regularSizeButton);
        buttons.add(boldStyleButton);
        buttons.add(regularStyleButton);
        buttons.add(closeWindowButton);
        return buttons;
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
}
