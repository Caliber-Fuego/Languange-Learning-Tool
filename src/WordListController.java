import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

public class WordListController {

    static wordBase wb = new wordBase("word");

    //FXML GUI Related
    @FXML private Button addWordButton, wordListButton, flashcardButton;
    @FXML private TableView<TextReader> wordListTable;
    @FXML private TableColumn<TextReader, String> wordColumn;
    @FXML private Label testLabel;

    public void initialize(){
        tableInitialize();
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
    private void tableInitialize(){
        //Handles the update of the tableColumns whenever it receives data
        wordColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getWord()));

        //Checks if the file exists and then adds whatever's in the file into the LinkedList
        //TODO: Have a method that lets the user creates their own wordlist and pass that file in here
        wb.fileCheck("word");
        LinkedList<TextReader> wordList = wb.readDataFromFile();

        //Sets the data in the table to whatever was in the LinkedList
        ObservableList<TextReader> observableWordList = FXCollections.observableList(wordList);
        wordListTable.setItems(observableWordList);

        //When a row is double-clicked, it lets the user see the word's definition
        //TODO: Create a popup for this
        wordListTable.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                TextReader selectedWord = wordListTable.getSelectionModel().getSelectedItem();
                if(selectedWord != null){
                    String definition = wb.fetchDefinition(selectedWord.getWord());
                    testLabel.setText(definition);
                }
            }
        });
    }

}
