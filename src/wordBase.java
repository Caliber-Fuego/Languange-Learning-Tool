import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
public class wordBase {

    String executablePath = "src/";

    //File Handling Related
    private File file = null;
    private FileWriter fWrite = null;

    //Constructor
    public wordBase(String filename){
        fileCheck(filename);
        file = new File(filename);
    }

    public wordBase(){
        fileCheck("word.csv");
        file = new File("word.csv");
    }

    //Appends strings to file
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

    //Reads data from a file and adds it on to a list
    public void readDataFromFile(LinkedList<TextReader> list){
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

    //Reads data from a file, puts the data on a list and returns the populated list
    public LinkedList<TextReader> readDataFromFile(){
        LinkedList<TextReader> wordData = new LinkedList<>();

        try {
            BufferedReader bRead = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bRead.readLine()) != null) {
                String word = line.trim();
                wordData.add(new TextReader(word));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return wordData;
    }

    //Checks if the file exists or not, and if it doesn't it creates the file and the directory
    public void fileCheck(String filename) {
        try {
            String dbFilePath = executablePath + "Word Decks/"+filename;
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

    // Function to fetch the definition with the api
    public List<String> fetchDefinition(String word) {
        //Gets the API url and passes the received word on to it
        List<String> definitionList = new ArrayList<>();
        String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //Handles the parsing of the JSON File
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader bRead = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = bRead.readLine()) != null) {
                    response.append(inputLine);
                }

                bRead.close();
            } else {
                // Handle HTTP error
                definitionList.add("Error: Unable to fetch definition");
                return definitionList;
            }

            JSONArray jsonArray = new JSONArray(response.toString());

            // Handles receiving the definitions and puts them into a JSONArray to add into the list
            if (jsonArray.length() > 0) {
                JSONObject firstEntry = jsonArray.getJSONObject(0);
                JSONArray meanings = firstEntry.getJSONArray("meanings");

                //Adds every definition into the list
                for(int i = 0; i < meanings.length(); i++){
                    JSONObject meaning = meanings.getJSONObject(i);
                    JSONArray definitions = meaning.getJSONArray("definitions");

                    for (int j = 0; j < definitions.length(); j++){
                        JSONObject definition = definitions.getJSONObject(j);
                        definitionList.add(definition.getString("definition"));
                    }
                }
                // Handle the case where there is no definition
                if (definitionList.isEmpty()){
                    definitionList.add("No definitions found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            definitionList.add("Error: Unable to fetch definition");
        }

        return definitionList;
    }
}
