import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.net.HttpURLConnection;
import java.net.URL;
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

    // Function to fetch the definition with the api
    public String fetchDefinition(String word) {
        //Gets the API url and passes the received word on to it
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
                // Handle HTTP error (e.g., by showing an error message in your GUI)
                return "Error: Unable to fetch definition";
            }

            JSONArray jsonArray = new JSONArray(response.toString());

            // Gets the first definition found in the string parsed
            // TODO: When the popup window is made, make it so that it lists every definition found instead
            if (jsonArray.length() > 0) {
                JSONObject firstEntry = jsonArray.getJSONObject(0);
                JSONArray meanings = firstEntry.getJSONArray("meanings");
                if (meanings.length() > 0) {
                    JSONObject firstMeaning = meanings.getJSONObject(0);
                    JSONArray definitions = firstMeaning.getJSONArray("definitions");
                    if (definitions.length() > 0) {
                        JSONObject firstDefinition = definitions.getJSONObject(0);
                        return firstDefinition.getString("definition");
                    }
                }
            }

            // Handle the case where there is no definition
            return "No definition found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to fetch definition";
        }
    }
}
