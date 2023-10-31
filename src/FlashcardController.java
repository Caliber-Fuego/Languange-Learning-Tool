import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class FlashcardController {

    @FXML private Button addWordButton, wordListButton, flashcardButton;

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

}
