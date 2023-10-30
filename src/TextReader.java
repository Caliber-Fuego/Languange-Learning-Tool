import java.util.Iterator;
import java.util.LinkedList;

public class TextReader {

    //Properties
    private String word;
    private TextReader next;
    static wordBase wb = new wordBase("word");

    //Constructor
    public TextReader(String word){
        this.word = word;
        this.next = null;
    }

    //Getters
    public String getWord() { return word; }
    public TextReader getNext() { return next; }

    //Setters
    public void setNext(TextReader next) { this.next = next; }


    //Functions
    //Adds words to the file
    //TODO: Make it so that adding duplicate words isn't allowed when running this method again
    public static void addWordToFile(String word, LinkedList<TextReader> list){
        wb.readFromFile(list);

        boolean isNewWord = true;

        for (TextReader node : list){
            if(node.getWord().equalsIgnoreCase(word)){
                isNewWord = false;
                System.out.println("The word \""+word+"\" exists!");
                break;
            }
        }

        if (isNewWord){
            TextReader newNode = new TextReader(word);
            newNode.setNext(list.isEmpty() ? null : list.getFirst());
            list.addFirst(newNode);
            try {
                wb.fileCheck("word");
                wb.writeWordToFile(word + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Extracts words from paragraphs and then adds it to the file
    public static LinkedList<TextReader> wordExtract(String text){
        wb.fileCheck("word");
        LinkedList<TextReader> list = new LinkedList<>();

        String[] words = text.split("[\\s]+");
        for (String word : words){
            word = word.replaceAll("[^a-zA-Z0-9'’]", "");
            if (!word.isEmpty()) {
                addWordToFile(word, list);
            }
        }
        return list;
    }
}
