import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class WordPopupController {

    @FXML private Label labelWord, labelDefinition;
    @FXML private  AnchorPane anchorPane;

    public void initialize() {
        labelDefinition.setMaxWidth(Region.USE_PREF_SIZE);
        labelDefinition.prefWidthProperty().bind(anchorPane.widthProperty().subtract(10));
    }

    public void setWordAndDefinition(String word, String definition) {
        labelWord.setText(word);
        labelDefinition.setText(definition);
    }
}
