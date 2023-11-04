package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class WordAddedPopupController {

    @FXML TextArea textArea;

    public void setTextArea(String text){
        textArea.setText(text);
    }
}
