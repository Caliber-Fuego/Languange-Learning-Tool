import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;


public class TextReaderController {
    TextReader read = new TextReader(null);

    @FXML private TextField txtReadField;
    @FXML private Button addWordButton, wordListButton, flashcardButton;


    @FXML
    protected void onTextAddClick(){
        read.wordExtract(txtReadField.getText());
    }

    @FXML
    protected void onWordListClick() throws IOException {
        // Load the TextReader.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WordListView.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Set the new scene
        Scene scene = new Scene(root);
        Stage stage = (Stage) wordListButton.getScene().getWindow();
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

}
