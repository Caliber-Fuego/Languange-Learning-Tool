package Model;

public class TextReaderJPNode extends TextReader{
    private String hiraganaForm;
    private String definition;
    private String word;
    private TextReaderJPNode next;

    public TextReaderJPNode() {
        super();
        word = null;
        hiraganaForm = null;
        definition = null;
        next = null;
    }

    public String getHiraganaForm() {
        return hiraganaForm;
    }

    public String getDefinition() {
        return definition;
    }
    public String getWord() {return word;}
    public TextReaderJPNode getNext() { return next; }


    public void setHiraganaForm(String hiraganaForm) {
        this.hiraganaForm = hiraganaForm;
    }

    public void setDefinition(String meaning) {
        this.definition = meaning;
    }
    public void setWord (String word) { this.word = word; }
    public void setNext(TextReaderJPNode next) { this.next = next; }

}
