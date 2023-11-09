package Controller;

import Model.TextReader;
import Model.wordBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;


public class TextReaderController {
    TextReader read = new TextReader(null);
    wordBase wb = new wordBase("");


    @FXML private TextArea txtReadField;
    @FXML private Button addWordButton, wordListButton, flashcardButton, createButton, deleteButton;
    @FXML private ComboBox wordListCBox, langCBox;

    public void initialize() {
        cBoxList();
        lBoxList();
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

    public void lBoxList(){
        ObservableList<String> languageList = FXCollections.observableArrayList();
        languageList.add("English");
        languageList.add("Japanese");

        langCBox.setItems(languageList);
        langCBox.setValue(languageList.get(0));
    }

    @FXML
    protected void onTextAddClick(){
        String lang = languageList();
        String hold = "";

        switch (lang){
            case "en":
                hold = TextReader.wordExtract(txtReadField.getText(), wordListCBox.getValue().toString(), lang);

                break;
            case "jp":
                hold = TextReader.wordExtractJP(txtReadField.getText(), wordListCBox.getValue().toString(), lang);

                break;
        }

        popupNewWordsShow(hold);
    }

    @FXML
    protected void onWordListClick() throws IOException {
        // Load the TextReader.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/WordListView.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/TextReaderView.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/FlashcardView.fxml"));
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
        popupCreateShow();
    }

    @FXML
    protected void onDeleteFileClick(){
        String selectedFile = wordListCBox.getValue().toString();

        int input = JOptionPane.showConfirmDialog(null, "Delete file "+selectedFile+"?");

        switch (input){
            case 0:
                wb.fileDelete(selectedFile);
                cBoxList();
                JOptionPane.showMessageDialog(null, "File deleted!");
                break;
            default:
                break;
        }
    }

    public void popupCreateShow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/WordListCreatePopup.fxml"));
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

    public void popupNewWordsShow(String text){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/WordAddedPopupView.fxml"));
            Parent root = loader.load();
            WordAddedPopupController wordAddedPopupController = loader.getController();

            //Sets the text for both the word and definition
            if(!(text == null)){
                wordAddedPopupController.setTextArea(text);
            }else {
                wordAddedPopupController.setTextArea("No new words added!");
            }


            // Create a new Stage for the popup
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));

            // Show the popup
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String languageList(){
        String lang = langCBox.getValue().toString();

        switch (lang){
            case "English":
                lang = "en";
                break;
            case "Japanese":
                lang = "jp";
                break;
        }

        return lang;
    }
}
