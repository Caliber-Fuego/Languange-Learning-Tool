import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WordListController {
    //FXML GUI Related
    @FXML private Button addWordButton, wordListButton, flashcardButton;
    @FXML private TableView<TextReader> wordListTable;
    @FXML private TableColumn<TextReader, String> wordColumn;
    @FXML private Label testLabel;
    @FXML private ComboBox wordListCBox;



    static wordBase wb = new wordBase();
    private String wordData = "", definitionData = "";

    public String getWordData() { return wordData; }
    public String getDefinitionData() { return definitionData; }


    public void initialize(){
        cBoxList();
        tableInitialize(wordListCBox.getValue().toString());
    }

    //Scene Transformers
    //i.e it changes windows whenever you click a button
    @FXML
    protected void onWordListClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WordListView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) wordListButton.getScene().getWindow(); // Assumes you have a reference to a control in the current scene
        stage.setScene(scene);
    }

    @FXML
    protected void onTextReadClick() throws IOException {
        // Load the TextReader.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TextReaderView.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Set the new scene
        Scene scene = new Scene(root);
        Stage stage = (Stage) addWordButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    protected void onFlashcardClick() throws IOException {
        // Load the TextReader.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FlashcardView.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Set the new scene
        Scene scene = new Scene(root);
        Stage stage = (Stage) flashcardButton.getScene().getWindow();
        stage.setScene(scene);
    }

    //Functions
    //Handles the table initialization
    private void tableInitialize(String fileName){
        //Handles the update of the tableColumns whenever it receives data
        wordColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getWord()));

        //Checks if the file exists and then adds whatever's in the file into the LinkedList
        //TODO: Have a method that lets the user creates their own wordlist and pass that file in here
        wb.fileCheck(fileName);
        LinkedList<TextReader> wordList = wb.readDataFromFile();

        //Sets the data in the table to whatever was in the LinkedList
        ObservableList<TextReader> observableWordList = FXCollections.observableList(wordList);
        wordListTable.setItems(observableWordList);

        //When a row is double-clicked, it lets the user see the word's definition through a popup
        wordListTable.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                TextReader selectedWord = wordListTable.getSelectionModel().getSelectedItem();
                if(selectedWord != null){
                    wordData = selectedWord.getWord();
                    List<String> definitionList = wb.fetchDefinition(wordData);
                    definitionData = String.join("\n\n", definitionList);
                    popupShow(wordData, definitionData);
                }
            }
        });
    }

    public void cBoxList(){
        // Directory path you want to read files from
        String directoryPath = "src/Word Decks/";

        // Create a File object for the directory
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            // List the files in the directory
            File[] files = directory.listFiles();

            if (files != null) {
                ObservableList<String> fileNames = FXCollections.observableArrayList();

                for (File file : files) {
                    if (file.isFile()) {
                        fileNames.add(file.getName());
                    }
                }

                wordListCBox.setItems(fileNames);
                wordListCBox.setValue(fileNames.get(0));
            }
        }

        // Add a ChangeListener to the ComboBox
        wordListCBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Handle the change of selected item
                tableInitialize(wordListCBox.getValue().toString());
            }
        });
    }

    public void popupShow(String word, String definition) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WordPopupView.fxml"));
            Parent root = loader.load();
            WordPopupController popupController = loader.getController();

            //Sets the text for both the word and definition
            popupController.setWordAndDefinition(word, definition);

            // Create a new Stage for the popup
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));

            // Show the popup
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
