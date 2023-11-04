import Model.TextReader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Application;


import java.io.IOException;

public class Main extends Application{
    static TextReader read = new TextReader(null);
    private BorderPane rootLayout;

    public void start(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/TextReaderView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Test!");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
        /*
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter a paragraph!");
        String text = sc.nextLine();

        LinkedList<TextReader> wordList = read.wordExtract(text);
         */
    }
}