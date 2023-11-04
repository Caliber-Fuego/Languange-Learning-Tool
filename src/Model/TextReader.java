package Model;

import java.util.LinkedList;

public class TextReader {

    //Properties
    private String word, language;
    private TextReader next;
    static wordBase wb = new wordBase("");

    //Constructor
    public TextReader(String word){
        this.word = word;
        this.next = null;
    }

    public TextReader(){
        this.word = "";
        this.next = null;
    }

    //Getters
    public String getWord() { return word; }
    public TextReader getNext() { return next; }

    //Setters
    public void setWord(String word) { this.word = word; }
    public void setNext(TextReader next) { this.next = next; }


    //Functions
    //Extracts words from paragraphs and then adds it to the file
    public static String wordExtract(String text, String filename, String language){
        wb.fileCheck(filename);
        String hold = "";
        LinkedList<TextReader> list = new LinkedList<>();

        String[] words = text.split("[\\s、。]+");
        for (String word : words){
            word = word.replaceAll("[^a-zA-Z0-9'’ぁ-んァ-ヶー一-龠々〆ヵヶ]", "");
            if (!word.isEmpty()) {
                hold += addWordToFile(word, list, filename, language);
            }
        }

        return hold;
    }

    //Adds words to the file
    public static String addWordToFile(String word, LinkedList<TextReader> list, String filename, String language){
        wb.readDataFromFile(list);
        String hold = "";

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
                hold = list.getFirst().getWord();
                wb.writeWordToFile(list.getFirst().getWord()+","+language+"\n");
                //TODO: turn this into a popup and have it list the new words added
                System.out.println("\""+list.getFirst().getWord()+"\" is added!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return hold+"\n";
    }
}
