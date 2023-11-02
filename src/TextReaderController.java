import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;


public class TextReaderController {
    TextReader read = new TextReader(null);

    @FXML private TextField txtReadField;
    @FXML private Button addWordButton, wordListButton, flashcardButton, createButton;
    @FXML private ComboBox wordListCBox;

    public void initialize() {
        cBoxList();
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
    }

    @FXML
    protected void onTextAddClick(){
        read.wordExtract(txtReadField.getText(), wordListCBox.getValue().toString());
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

    @FXML
    protected void onCreateListClick(){
        popupShow();
    }

    public void popupShow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WordListCreatePopup.fxml"));
            Parent root = loader.load();

            // Create a new Stage for the popup
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));

            WordListCreateController controller = loader.getController();
            controller.trc = this;
            controller.setStage(popupStage);

            // Show the popup
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
