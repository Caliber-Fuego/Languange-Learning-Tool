import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class wordBase {

    String executablePath = "src/";

    //File Handling Related
    private File file = null;
    private FileWriter fWrite = null;

    public wordBase(String filename){
        fileCheck(filename);
        file =new File(filename);
    }

    public void writeToFile(LinkedList<TextReader> linkedList){
        try {
            fWrite = new FileWriter(file);
            TextReader current = linkedList.getFirst();
            while (current != null){
                fWrite.write(current.getWord()+"\n");
                current = current.getNext();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void writeWordToFile(String text){
        try{
            fWrite = new FileWriter(file, true);
            fWrite.write(text);

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (fWrite != null) {
                    fWrite.close(); // Close the FileWriter to save changes
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readFromFile(LinkedList<TextReader> list){
        try{
            BufferedReader bRead = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bRead.readLine()) != null ){
                String word = line.trim();

                TextReader newNode = new TextReader(word);
                newNode.setNext(list.isEmpty() ? null : list.getFirst());
                list.addFirst(newNode);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void fileCheck(String filename) {
        try {
            String dbFilePath = executablePath + "Word Decks/"+filename+".csv";
            this.file = new File(dbFilePath);

            if (!file.exists()) {
                //Creates Directory if directory does not exist
                String dbFolderPath = executablePath + "Word Decks";
                Files.createDirectories(Paths.get(dbFolderPath));

                //Creates File if file does not exist
                Files.createFile(Paths.get(dbFilePath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
