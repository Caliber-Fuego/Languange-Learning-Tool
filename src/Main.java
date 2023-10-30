import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    static TextReader read = new TextReader(null);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter a paragraph!");
        String text = sc.nextLine();

        LinkedList<TextReader> wordList = read.wordExtract(text);
    }
}