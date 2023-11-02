import java.util.LinkedList;

public class TextReader {

    //Properties
    private String word;
    private TextReader next;
    static wordBase wb = new wordBase("");

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

    //Extracts words from paragraphs and then adds it to the file
    public static void wordExtract(String text, String filename){
        wb.fileCheck(filename);
        LinkedList<TextReader> list = new LinkedList<>();

        String[] words = text.split("[\\s]+");
        for (String word : words){
            word = word.replaceAll("[^a-zA-Z0-9'’]", "");
            if (!word.isEmpty()) {
                addWordToFile(word, list, filename);
            }
        }
    }

    //Adds words to the file
    public static void addWordToFile(String word, LinkedList<TextReader> list, String filename){
        wb.readDataFromFile(list);

        boolean isNewWord = true;

        //Checks if the word added is in the list, if it is it sets the boolean value to false
        for (TextReader node : list){
            if(node.getWord().equalsIgnoreCase(word)){
                isNewWord = false;
                System.out.println("The word \""+word+"\" exists!");
                break;
            }
        }

        //If the word added is new, it adds it into the list
        //TODO: create a temporary list to show the new words added in a popup
        if (isNewWord){
            TextReader newNode = new TextReader(word);
            newNode.setNext(list.isEmpty() ? null : list.getFirst());
            list.addFirst(newNode);
            try {
                wb.fileCheck(filename);
                wb.writeWordToFile(word + "\n");
                //TODO: turn this into a popup and have it list the new words added
                System.out.println("\""+word+"\" is added!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
