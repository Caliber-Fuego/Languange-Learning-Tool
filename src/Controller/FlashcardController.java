package Controller;

import Model.wordBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import java.util.concurrent.atomic.AtomicInteger;

public class FlashcardController {

    @FXML private Button addWordButton, wordListButton, flashcardButton, answerButton;
    @FXML private Label quizDefinition, quizCorrect, quizWrong, labelNameA, labelNameB;
    @FXML private TableView<String> listTable;
    @FXML private TableColumn<String, String> listColumn;
    @FXML private ScrollPane quizPane;
    @FXML private TextField quizAnswer;

    private ObservableList<String> fileNames = FXCollections.observableArrayList();
    static wordBase wb = new wordBase();
    int correct = 0, wrong = 0;
    String[] result;
    String selectedFile = "";


    public void initialize() {
        listTableInitialize();
        quizPane.setVisible(false);
        quizAnswer.setVisible(false);
        quizCorrect.setVisible(false);
        quizWrong.setVisible(false);
        labelNameA.setVisible(false);
        labelNameB.setVisible(false);
        answerButton.setVisible(false);
    }

    private void listTableInitialize(){
        String directoryPath = "src/Word Decks/";

        loadFileNamesFromDirectory(directoryPath);

        listColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        listTable.setItems(fileNames);

        listTable.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                selectedFile = listTable.getSelectionModel().getSelectedItem();

                if(selectedFile != null){
                    quizPane.setVisible(true);
                    quizAnswer.setVisible(true);
                    quizCorrect.setVisible(true);
                    quizWrong.setVisible(true);
                    labelNameA.setVisible(true);
                    labelNameB.setVisible(true);
                    answerButton.setVisible(true);
                    listTable.setVisible(false);

                    result = wb.fetchRandomWordAndDefinition(selectedFile);
                    quizDefinition.setText(result[1]);
                }
            }
        });
    }

    @FXML
    protected void onAnswerClick(){
        // Check the answer and update the correct and wrong counts
        if (quizAnswer.getText().equalsIgnoreCase(result[0])) {
            correct++;
        } else {
            wrong++;
        }

        quizCorrect.setText(String.valueOf(correct));
        quizWrong.setText(String.valueOf(wrong));

        result = wb.fetchRandomWordAndDefinition(selectedFile);
        quizDefinition.setText(result[1]);

        quizAnswer.clear();

        if (correct + wrong >= 10) {
            answerButton.setDisable(true);
            listTable.setVisible(true);
            quizPane.setVisible(false);
            quizAnswer.setVisible(false);
            quizCorrect.setVisible(false);
            quizWrong.setVisible(false);
            labelNameA.setVisible(false);
            labelNameB.setVisible(false);
            answerButton.setVisible(false);
        }
    }



    @FXML
    protected void onWordListClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/WordListView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) wordListButton.getScene().getWindow(); // Assumes you have a reference to a control in the current scene
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

    private void loadFileNamesFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileNames.add(file.getName());
                    }
                }
            }
        }
    }

}
