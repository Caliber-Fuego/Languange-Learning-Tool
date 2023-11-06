package Controller;

import Model.TextReader;
import Model.wordBase;
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

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class WordListController {
    //FXML GUI Related
    @FXML private Button addWordButton, wordListButton, flashcardButton, deleteButton;
    @FXML private TableView<TextReader> wordListTable, historyTable;
    @FXML private TableColumn<TextReader, String> wordColumn, historyColumn;
    @FXML private Label testLabel;
    @FXML private TextField searchBar;
    @FXML private ComboBox wordListCBox;

    static wordBase wb = new wordBase();
    private Stack<String> wordHistory = new Stack<>();
    private String wordData = "", definitionData = "", hiraganaData = "";
    public String getWordData() { return wordData; }
    public String getDefinitionData() { return definitionData; }


    public void initialize(){
        cBoxList();
        tableInitialize(wordListCBox.getValue().toString());
        historyTableInitialize();
        wb.loadWordHistory(wordHistory);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            updateTableWithSearch(newValue);
        });
    }

    //Scene Transformers
    @FXML
    protected void onWordListClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/WordListView.fxml"));
        Parent root = loader.load();
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
    protected void onDeleteClick(){
        String word = wordListTable.getSelectionModel().getSelectedItem().getWord();

        int input = JOptionPane.showConfirmDialog(null, "Delete the word \""+word+"\"?");
        switch (input){
            case 0:
                wb.fileCheck(wordListCBox.getValue().toString());
                wb.deleteWordFromFile(word, wordListCBox.getValue().toString());
                tableInitialize(wordListCBox.getValue().toString());
                JOptionPane.showMessageDialog(null, "Word deleted!");
                break;
            default:
                break;
        }
    }


    //Functions
    //Handles the table initialization
    private void tableInitialize(String fileName){
        //Handles the update of the tableColumns whenever it receives data
        wb.fileCheck(fileName);
        wordColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getWord()));

        //Checks if the file exists and then adds whatever's in the file into the LinkedList
        LinkedList<TextReader> wordList = wb.readDataFromFile();

        //Sets the data in the table to the contents of the LinkedList
        ObservableList<TextReader> observableWordList = FXCollections.observableList(wordList);
        wordListTable.setItems(observableWordList);

        //When a row is double-clicked, it lets the user see the word's definition through a popup
        wordListTable.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                TextReader selectedWord = wordListTable.getSelectionModel().getSelectedItem();
                wb.fileCheck(fileName);
                String langData = wb.getWordLanguage(selectedWord.getWord());

                if(selectedWord != null){
                    switch(langData){
                        case "en":
                            wordData = selectedWord.getWord();
                            List<String> definitionList = wb.fetchDefinition(wordData);
                            definitionData = String.join("\n\n", definitionList);
                            addToWordHistory(wordData, langData);
                            historyTableInitialize();
                            popupShow(wordData, definitionData, hiraganaData);

                            break;
                        case "jp":
                            try{
                                String[] result = wordBase.searchInDictionary(selectedWord.getWord());
                                wordData = selectedWord.getWord();
                                definitionData = "English Definitions: \n\n"+result[2].replaceAll("[\\[\\],]", "");
                                hiraganaData = result[1];
                                addToWordHistory(wordData, langData);
                                historyTableInitialize();
                                popupShow(wordData, definitionData, hiraganaData);
                            }catch (NullPointerException e){
                                wordData = selectedWord.getWord();
                                definitionData = "No definitions found!";
                                addToWordHistory(wordData, langData);
                                historyTableInitialize();
                                popupShow(wordData, definitionData, hiraganaData);
                            }
                            break;
                        default:
                            System.out.println("langData: " + langData);
                            System.out.println("what?");
                    }
                }
            }
        });
    }

    private void historyTableInitialize(){
        //Handles the update of the tableColumns whenever it receives data
        wb.fileCheck("History/wordHistory.txt");
        historyColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getWord()));

        //Checks if the file exists and then adds whatever's in the file into the LinkedList
        LinkedList<TextReader> wordList = wb.readDataFromFile();

        //Reverses the linkedList
        Collections.reverse(wordList);

        //Sets the data in the table to whatever was in the LinkedList
        ObservableList<TextReader> observableWordList = FXCollections.observableList(wordList);
        historyTable.setItems(observableWordList);

        //When a row is double-clicked, it lets the user see the word's definition through a popup
        historyTable.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                TextReader selectedWord = historyTable.getSelectionModel().getSelectedItem();
                String langData = wb.getWordLanguage(selectedWord.getWord());

                if(selectedWord != null){

                    switch(langData){
                        case "en":
                            wordData = selectedWord.getWord();
                            List<String> definitionList = wb.fetchDefinition(wordData);
                            definitionData = String.join("\n\n", definitionList);
                            addToWordHistory(wordData, langData);
                            historyTableInitialize();
                            popupShow(wordData, definitionData, hiraganaData);
                            break;
                        case "jp":
                            try{
                                String[] result = wordBase.searchInDictionary(selectedWord.getWord());
                                wordData = selectedWord.getWord();
                                definitionData = "English Definitions: \n\n"+result[2].replaceAll("[\\[\\],]", "");
                                hiraganaData = result[1];
                                addToWordHistory(wordData, langData);
                                historyTableInitialize();
                                popupShow(wordData, definitionData, hiraganaData);
                            }catch (NullPointerException e){
                                wordData = selectedWord.getWord();
                                definitionData = "No definitions found!";
                                addToWordHistory(wordData, langData);
                                historyTableInitialize();
                                popupShow(wordData, definitionData, hiraganaData);
                            }
                            break;
                        default:
                            System.out.println("what?");
                    }
                }
            }
        });
    }

    private void updateTableWithSearch(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            // If search text is empty, show all items
            tableInitialize(wordListCBox.getValue().toString());
        } else {
            // Filter the items based on the search text
            ObservableList<TextReader> filteredList = wordListTable.getItems().filtered(textReader ->
                    textReader.getWord().toLowerCase().contains(searchText.toLowerCase())
            );

            wordListTable.setItems(filteredList);
        }
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
                try{
                    tableInitialize(wordListCBox.getValue().toString());
                }catch (NullPointerException e){
                    System.out.println("Table is empty!");
                }
            }
        });
    }

    public void popupShow(String word, String definition, String hiragana) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/WordPopupView.fxml"));
            Parent root = loader.load();
            WordPopupController popupController = loader.getController();

            //Sets the text for both the word and definition
            popupController.setWordAndDefinition(word, definition, hiragana);

            // Create a new Stage for the popup
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));

            // Show the popup
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToWordHistory(String word, String lang){
        wordHistory.push(word);
        historyTableInitialize();
        wb.saveWordHistory(wordHistory, lang);
    }
}
