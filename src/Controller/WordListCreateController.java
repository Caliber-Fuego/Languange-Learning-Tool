package Controller;

import Model.wordBase;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WordListCreateController {
    @FXML TextField textField;
    @FXML Button createButton;

    wordBase wb = new wordBase();
    TextReaderController trc;

    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void createWordList(){
        wb.fileCheck(textField.getText()+".csv");
        if (stage != null) {
            trc.cBoxList();
            stage.close();
        }
    }
}
