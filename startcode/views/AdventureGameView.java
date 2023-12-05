package views;

import AdventureModel.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;

import java.io.File;
import java.util.*;


/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 * ZOOM LINK: https://utoronto-my.sharepoint.com/:v:/g/personal/sajawal_nauman_mail_utoronto_ca/EeO5Isy6H6tKjGsxDBvihOkBASPWQ9TN89bToxT9mTGt2g    (SHAREPOINT LINK)
 * PASSWORD: <PASSWORD HERE>    (UofT Account will have access)
 */
public class AdventureGameView implements Observer{

    AdventureGame model; //model of the game
    public Stage stage; //stage on which all is rendered
    static Button saveButton, loadButton, helpButton, leaderboardButton, settingButton, pauseResumeButton; //buttons
    Boolean helpToggle = false; //is help on display?
    Boolean leaderboardToggle = false; //is leaderboard on display?

    GridPane gridPane = new GridPane(); //to hold images and buttons
    static Label roomDescLabel = new Label(); //to hold room description and/or instructions

    static Label objLabel, invLabel; //to hold object and inventory description and/or instructions
    static Label commandLabel = new Label(); //to hold command description and/or instructions
    static Label leaderboardLabel = new Label(); //to hold leaderboard
    static Label instructionLabel = new Label(); //to hold instruction

    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    VBox textEntry = new VBox(); // to hold inputTextField
    ImageView roomImageView; //to hold room image
    TextField inputTextField = new TextField(); //for user input

    private static int currentFontSize = 16; //to keep font size information
    private static String currentFontStyle; //to keep font style information

    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing

    private Timeline timeline; // to update the time of the game timer
    static Label remainTimeLabel = new Label(); // to display the remain time of the game timer

    SaveView currentSaveView; // Represents the current SaveView instance for managing the save game GUI.
    LoadView currentLoadView; // Represents the current LoadView instance for handling the load game GUI.

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
        startTimer();
        this.stage.setOnCloseRequest(event -> {
            stopTimer();
            this.model.gameTimer.stopTimer();
            System.exit(0);
        });
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("naumans2's Adventure Game"); //Replace <YOUR UTORID> with your UtorID

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(140);
        ColumnConstraints column2 = new ColumnConstraints(800);
        ColumnConstraints column3 = new ColumnConstraints(140);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints(100);
        RowConstraints row2 = new RowConstraints( 500 );
        RowConstraints row3 = new RowConstraints(30);
        RowConstraints row4 = new RowConstraints(130);
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );
        row4.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row3, row4 );

        gridPane.setHgap(30);

        // scrollable window
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);

        // VBox that holds the scrollPane
        VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(scrollPane);

        // Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 170, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setId("Leaderboard");
        customizeButton(leaderboardButton, 200, 50);
        makeButtonAccessible(leaderboardButton, "Leaderboard Button", "This button shows the leaderboard.", "This button shows the leaderboard. Click it to see the top 3 times.");
        addLeaderboardEvent();

        settingButton = new Button("Setting");
        settingButton.setId("Setting");
        customizeButton(settingButton, 150,50);
        makeButtonAccessible(settingButton, "Setting Button", "This button sets the font size.", "This button adjust the font size and style. Click it in order to adjust the game font size and style.");
        addSettingEvent();

        //Pause and Resume Button
        pauseResumeButton = new Button("Pause/Resume");
        pauseResumeButton.setId("Pause/Resume");
        pauseResumeButton.setFont(new Font("Arial", currentFontSize));
        setFontStyleButton(currentFontStyle, pauseResumeButton);
        customizeButton(pauseResumeButton, 200, 50);
        makeButtonAccessible(pauseResumeButton, "Pause and Resume Button", "This button used to pause and resume the timer.", "This button allows you to pause or resume the timer. Click it to toggle between pause and resume modes.");
        addPauseResumeEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, helpButton, loadButton);
        HBox bottomButtons = new HBox();
        bottomButtons.getChildren().addAll(pauseResumeButton, leaderboardButton, settingButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);
        bottomButtons.setSpacing(10);
        bottomButtons.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox();
        buttonBox.getChildren().addAll(topButtons, bottomButtons);
        buttonBox.setSpacing(10);

        inputTextField.setFont(new Font("Arial", currentFontSize));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setWrapText(true);
        objLabel.setTextOverrun(OverrunStyle.CLIP);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", currentFontSize));
        setFontStyleLabel(currentFontStyle, objLabel);

        invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setWrapText(true);
        invLabel.setTextOverrun(OverrunStyle.CLIP);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", currentFontSize));
        setFontStyleLabel(currentFontStyle, invLabel);

        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( buttonBox, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

        commandLabel.setText("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", currentFontSize));
        setFontStyleLabel(currentFontStyle, commandLabel);

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms

        // adding the text area and submit button to a VBox
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 3, 3, 1 );

        // Render everything
        var scene = new Scene( mainLayout ,  1200, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * addPauseResumeEvent()
     * __________________________
     * Add an event handler to the pauseResumeButton.
     *
     * When the button is clicked, it checks the current state
     * of the game timer and performs the following actions:
     * - If the game timer is running,
     * it pauses the game by changing its state, stopping the timer
     * - If the game timer is paused,
     * it resumes the game by changing its state, starting the timer
     */
    public void addPauseResumeEvent() {
        pauseResumeButton.setOnAction(e -> {
            if (this.model.gameTimer.isRunning()) {
                this.model.changeState();
                stopTimer();
                inputTextField.setDisable(true);
                objectsInRoom.setDisable(true);
                objectsInInventory.setDisable(true);
            } else {
                this.model.changeState();
                startTimer();
                inputTextField.setDisable(false);
                objectsInRoom.setDisable(false);
                objectsInInventory.setDisable(false);
            }
        });
    }

    /**
     * updateTimerLabel()
     * __________________________
     * Update TimerLabel as remain time is updating
     * if remaining Time is less than 0
     * stop the timer and exit the game.
     * */
    public void updateTimerLabel() {
        gridPane.getChildren().remove(remainTimeLabel);
        int remainingTime = this.model.gameTimer.getRemainingTime();
        remainTimeLabel.setText("Time Remaining: " + Integer.toString(remainingTime) + " seconds");
        remainTimeLabel.setStyle("-fx-text-fill: white;");
        remainTimeLabel.setFont(new Font("Arial", currentFontSize));
        setFontStyleLabel(currentFontStyle, remainTimeLabel);
        VBox timeBox = new VBox(remainTimeLabel);
        timeBox.setAlignment(Pos.BOTTOM_CENTER);
        gridPane.add(timeBox, 1, 2, 1, 1);
        if (remainingTime <= 0) {
            stopTimer();
            this.model.gameTimer.stopTimer();
            Platform.exit();
        }
    }

    /**
     * startTimer()
     * __________________________
     * Starts timeline and scheduling a timeline to regularly update the timer label.
     * If the timer is already running, it is stopped before starting again.
     */
    public void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateTimerLabel();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * stopTimer()
     *  __________________________
     * Halts the timeline updates.
     */
    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }


    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", currentFontSize));
        setFontStyleButton(currentFontStyle, inputButton);
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute
     *
     * Your event handler should respond when users
     * hits the ENTER or TAB KEY. If the user hits
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus
     * of the scene onto any other node in the scene
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {

        this.inputTextField.setOnKeyPressed(e -> {

            KeyCode keyPress = e.getCode();
            if (keyPress.equals(KeyCode.ENTER)) {
                submitEvent(this.inputTextField.getText().strip());
                this.inputTextField.setText("");
            }
            else if (keyPress.equals(KeyCode.TAB)) {
                this.gridPane.requestFocus();
                this.inputTextField.setText("");
            }
        });
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (!objectString.isEmpty()) roomDescLabel.setText(roomDesc + "\n\nObjects in this room:\n" + objectString);
            articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        }

        //try to move!
        String output = this.model.interpretAction(text); //process the command!

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            updateScene(output);
            updateItems();
        } else if (output.equals("GAME OVER")) {
            updateScene("");
            updateItems();

            gridPane.getChildren().remove(textEntry);

            inputTextField.setAccessibleText("Enter your name in this box.");
            inputTextField.setAccessibleHelp("This is the area in which you can enter your name. Enter your name and hit return to continue.");
            saveAttempt();

            commandLabel.setText("Please enter your name:");
            setFontStyleLabel(currentFontStyle, commandLabel);

            gridPane.add( textEntry, 0, 3, 3, 1 );

            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                this.model.gameTimer.stopTimer();
                stopTimer();
                Platform.exit();
            });
            pause.play();
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            //Your code will need to display the image in the
            //current room and pause, then transition to
            //the forced room.
            updateScene("");
            updateItems();
            inputTextField.setDisable(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
            pause.setOnFinished(event -> {
                inputTextField.setDisable(false);
                submitEvent(output);
            });
            pause.play();
        }
    }

    /**
     * saveAttempt
     * __________________________
     *
     * Upon pressing Enter, save the current player's name and time
     * into the leaderboard and update the leaderboard file to represent the change.
     */
    private void saveAttempt() {

        this.inputTextField.setOnKeyPressed(e -> {

            KeyCode keyPress = e.getCode();
            if (keyPress.equals(KeyCode.ENTER)) {
                this.model.getLeaderboard().addTime(this.inputTextField.getText(), this.model.getGameTimer().getEndingTime());
                this.model.saveLeaderboard();
                this.inputTextField.setText("");
            }
            else if (keyPress.equals(KeyCode.TAB)) {
                this.gridPane.requestFocus();
                this.inputTextField.setText("");
            }
        });
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the
     * current room.
     */
    private void showCommands() {
        roomDescLabel.setText("You can move in these directions:\n\n" + this.model.player.getCurrentRoom().getCommands());
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     *
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {

        getRoomImage(); //get the image of the current room
        formatText(textToDisplay); //format the text to display
        roomDescLabel.setPrefWidth(700);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        VBox roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");

        gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();

        //finally, articulate the description
        if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();
    }

    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     *
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            if (objectString != null && !objectString.isEmpty()) {
                roomDescLabel.setText(roomDesc + "\nObjects in this room:\n" + objectString);
                roomDescLabel.setFont(new Font("Arial", currentFontSize));
                setFontStyleLabel(currentFontStyle, roomDescLabel);
            }
            else {
                roomDescLabel.setText(roomDesc);
                roomDescLabel.setFont(new Font("Arial", currentFontSize));
                setFontStyleLabel(currentFontStyle, roomDescLabel);
            }
        } else roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Arial", currentFontSize));
        setFontStyleLabel(currentFontStyle, roomDescLabel);
        roomDescLabel.setAlignment(Pos.CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place
     * it in the roomImageView
     */
    private void getRoomImage() {

        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";

        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     *
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {

        //write some code here to add images of objects in a given room to the objectsInRoom Vbox

        objectsInRoom.getChildren().clear();
        objectsInInventory.getChildren().clear();

        for (AdventureObject obj: this.model.player.getCurrentRoom().objectsInRoom) {
            Button button = getButton(obj);
            button.setOnMouseClicked(e -> {
                submitEvent("TAKE " + obj.getName());
            });
            makeButtonAccessible(button, obj.getName(), obj.getDescription(),  "This button is a " + obj.getName()
                    + " object in the room. Click this button to add this object to your inventory.");
            objectsInRoom.getChildren().add(button);
        }

        for (AdventureObject obj: this.model.player.inventory) {
            Button button = getButton(obj);
            button.setOnMouseClicked(e -> {
                submitEvent("DROP " + obj.getName());
            });
            makeButtonAccessible(button, obj.getName(), obj.getDescription(),  "This button is a " + obj.getName()
                    + " object in your inventory. Click this button to drop this object in this room.");
            objectsInInventory.getChildren().add(button);
        }

        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox
        //please use setAccessibleText to add "alt" descriptions to your images!
        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";

        ScrollPane scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI,2,1);
    }

    /**
     * Creates a JavaFX Button representing an AdventureObject.
     * The button is configured with the name of the AdventureObject as its text,
     * an image corresponding to the AdventureObject's name, and additional properties.
     *
     * @param obj The AdventureObject for which the button is created.
     * @return A configured JavaFX Button representing the AdventureObject.
     */
    private Button getButton(AdventureObject obj) {
        Button button = new Button(obj.getName());
        button.setId(obj.getName());
        button.setContentDisplay(ContentDisplay.TOP);

        String buttonImage = this.model.getDirectoryName() + "/objectImages/" + obj.getName() + ".jpg";
        Image buttonImageFile = new Image(buttonImage);
        ImageView buttonImageView = new ImageView(buttonImageFile);
        buttonImageView.setPreserveRatio(true);
        buttonImageView.setFitWidth(100);
        button.setGraphic(buttonImageView);

        return button;
    }

    /**
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        String instruction = this.model.getInstructions();
        instructionLabel.setText(instruction);
        instructionLabel.setStyle("-fx-text-fill: white;");
        instructionLabel.setFont(new Font("Arial", currentFontSize));
        setFontStyleLabel(currentFontStyle, instructionLabel);
        instructionLabel.setWrapText(true);
        instructionLabel.setTextOverrun(OverrunStyle.CLIP);
        instructionLabel.setMaxWidth(560);

        VBox instructionBox = new VBox();
        instructionBox.getChildren().add(instructionLabel);
        instructionBox.setStyle("-fx-background-color: #000000;");
        instructionBox.setPadding(new Insets(10));
        instructionBox.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(instructionBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox popupVBox = new VBox();
        popupVBox.getChildren().add(scrollPane);
        Scene popupScene = new Scene(popupVBox, 600, 600); // Set the preferred size of the pop-up window

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Game Instructions");
        popupStage.setScene(popupScene);

        if (!helpToggle) {
            popupStage.show(); // Show the pop-up window and wait for it to close
            helpToggle = true;
        }
        popupStage.setOnCloseRequest(event -> {helpToggle = false;});
    }


    /** showLeaderboard
     * Show the game's leaderboard.
     */
    public void showLeaderboard() {

        if (!leaderboardToggle) {

            roomImageView.setImage(null);

            StringBuilder source = new StringBuilder();

            source.append("LEADERBOARD\n\n\n");

            HashMap sortedMap = new HashMap<String, Integer>();
            sortedMap = sortByValue(Leaderboard.bestTimes);

            for (Object key : sortedMap.keySet()) {
                source.append(key).append(" : ").append(Leaderboard.bestTimes.get(key)).append("s\n");
            }

            String text = source.toString();
            leaderboardLabel.setText(text);
            formatText(text); //format the text to display
            leaderboardLabel.setPrefWidth(1000);
            leaderboardLabel.setPrefHeight(1000);
            leaderboardLabel.setFont(new Font("Arial", currentFontSize));
            setFontStyleLabel(currentFontStyle, leaderboardLabel);
            leaderboardLabel.setStyle("-fx-text-fill:WHITE;");
            leaderboardLabel.setTextOverrun(OverrunStyle.CLIP);
            leaderboardLabel.setWrapText(true);
            leaderboardLabel.setAlignment(Pos.CENTER);

            VBox roomPane = new VBox(leaderboardLabel);
            roomPane.setPadding(new Insets(10));
            roomPane.setAlignment(Pos.TOP_CENTER);
            roomPane.setStyle("-fx-background-color: #000000;");

            gridPane.add(roomPane, 1, 1);
            updateTimerLabel();
        }

        else {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();

            updateScene(roomDesc + "\n\nObjects in this room:\n" + objectString);
            updateTimerLabel();
        }
        leaderboardToggle = !leaderboardToggle;
    }

    /** sortByValue
     * Helper method to sort and return the Hashmap based on the time(integer value).
     */
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {

        List<HashMap.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    /**
     * Retrieves a list containing all the Label objects associated with the current class.
     * The labels include room description, object description, inventory details, and command information.
     *
     * @return A List of Label objects representing various informational elements in the class.
     */
    private static List<Label> getAllLabels() {
        List<Label> labels = new ArrayList<>();
        labels.add(roomDescLabel);
        labels.add(objLabel);
        labels.add(invLabel);
        labels.add(commandLabel);
        labels.add(remainTimeLabel);
        labels.add(leaderboardLabel);
        labels.add(instructionLabel);
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
        buttons.add(saveButton);
        buttons.add(loadButton);
        buttons.add(helpButton);
        buttons.add(settingButton);
        buttons.add(leaderboardButton);
        buttons.add(pauseResumeButton);
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
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    /**
     * This method handles the event related to the
     * leaderbaord button.
     */
    public void addLeaderboardEvent() {
        leaderboardButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showLeaderboard();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            gridPane.requestFocus();
            SaveView saveView = new SaveView(this);
            currentSaveView = saveView;
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
            currentLoadView = loadView;
        });
    }

    /**
     * This method handles the event related to the
     * setting button.
     */
    public void addSettingEvent() {
        settingButton.setOnAction(e -> {
            gridPane.requestFocus();
            SettingView settingView = new SettingView(this, currentSaveView, currentLoadView);
        });
    }


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();

        if (!this.model.getPlayer().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        musicFile = musicFile.replace(" ","-");

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;

    }

    /**
     * This method stops articulations
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }
}