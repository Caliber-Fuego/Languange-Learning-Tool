package Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
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

    //Reads data from a file and adds it on to a LinkedList
    public void readDataFromFile(LinkedText<TextReader> list){
        try{
            BufferedReader bRead = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bRead.readLine()) != null ){
                String[] split = line.split(",");
                String word = split[0];

                TextReader newNode = new TextReader(word);
                newNode.setNext(list.isEmpty() ? null : list.getFirst());
                list.addAtFirst(newNode);
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
            while ((line = bRead.readLine()) != null ){
                String[] split = line.split(",");
                String word = split[0];

                wordData.add(new TextReader(word));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return wordData;
    }

    public void deleteWordFromFile(String word, String filename) {
        String dbFilePath = executablePath + "Word Decks/"+filename;
        this.file = new File(dbFilePath);
        List<String> updatedData = new ArrayList<>();

        try {
            BufferedReader bRead = new BufferedReader(new FileReader(file));

            String line;
            while ((line = bRead.readLine()) != null) {
                String[] split = line.split(",");

                if (!split[0].equalsIgnoreCase(word)) {
                    updatedData.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter bWrite = new BufferedWriter(new FileWriter(file));
            for (String line : updatedData) {
                bWrite.write(line);
                bWrite.newLine();
                bWrite.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getWordLanguage(String word){
        String lang = "";

        try {
            BufferedReader bRead = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bRead.readLine()) != null ){
                String[] split = line.split(",");
                if(split[0].equalsIgnoreCase(word)){
                    lang = split[1];
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return lang;
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

    public void fileDelete(String filename){
        try {
            String dbFilePath = executablePath + "Word Decks/"+filename;
            File fileDelete = new File(dbFilePath);

            if (fileDelete.exists()) {
                boolean deleted = fileDelete.delete();
                if (deleted) {
                    // File was successfully deleted
                    System.out.println("File deleted.");
                } else {
                    // Deletion failed
                    System.err.println("File deletion failed.");
                }

            } else {
                System.out.println("File does not exist: " + dbFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadWordHistory(Stack<String> s) {
        String historyFilePath = executablePath + "Word Decks/History/wordHistory.txt";
        fileCheck("History/wordHistory.txt");
        File historyFile = new File(historyFilePath);

        if (historyFile.exists()) {
            try (BufferedReader bRead = new BufferedReader(new FileReader(historyFile))) {
                String line;
                while ((line = bRead.readLine()) != null ){
                    String[] split = line.split(",");
                    s.push(split[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveWordHistory(Stack<String> s, String lang) {
        String historyFilePath = executablePath + "Word Decks/History/wordHistory.txt";
        fileCheck("History/wordHistory.txt");
        File historyFile = new File(historyFilePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile))) {
            for (String word : s) {
                writer.write(word+","+lang);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    public static String[] searchInDictionary(String word) {
        List<String> fileNames = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            fileNames.add("src/Model/jmdict_english/term_bank_" + i + ".json");
        }

        for (String fileName : fileNames) {
            LinkedList<TextReaderJPNode> nodes = readDataFromFile(fileName);
            for (TextReaderJPNode node : nodes) {
                if (node.getWord().equals(word)) {
                    return new String[]{node.getHiraganaForm(), node.getDefinition()};
                }
            }
        }

        return null;
    }

 */
    /*
    // method for the linear search algorithm
    public static String[] searchInDictionary(String word) {
        List<String> fileNames = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            fileNames.add("src/Model/jmdict_english/term_bank_" + i + ".json");
        }

        for (String fileName : fileNames) {
            String[] results = readDataFromFile(fileName, word);
            if (results != null) {
                return results; // Return the results if found
            }
        }

        return null;
    }

    */

    // Puts the files into a wordmap in sets
    public static String[] searchInDictionary(String word) {
        List<String> fileNames = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            fileNames.add("src/Model/jmdict_english/term_bank_" + i + ".json");
        }

        List<String> currentFiles = new ArrayList<>();
        int filesPerSet = 1;

        for (String fileName : fileNames) {
            currentFiles.add(fileName);

            if (currentFiles.size() >= filesPerSet) {
                TextHashMap<String, JSONArray> wordMap = insertDataIntoMap(currentFiles);
                currentFiles.clear();

                String[] results = readDataFromFile(wordMap, word);
                if (results != null) {
                    return results; // Return the results if found
                }
            }
        }

        // Check if there are any remaining files in the last set
        if (!currentFiles.isEmpty()) {
            TextHashMap<String, JSONArray> wordMap = insertDataIntoMap(currentFiles);
            String[] results = readDataFromFile(wordMap, word);
            if (results != null) {
                return results; // Return the results if found
            }
        }

        return null;
    }


    //Linearly searches for the Japanese Word inside a json file
    /*
    public static String[] readDataFromFile(String fileName, String word) {
        String[] wordTerm = null;

        try {
            // Provide the path to your JSON file
            FileReader reader = new FileReader(fileName);
            StringBuilder jsonString = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                jsonString.append((char) character);
            }

            // Parse the JSON array
            JSONArray jsonArray = new JSONArray(jsonString.toString());

            long start = System.nanoTime();
            // Iterate through the array and access the data
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonEntry = jsonArray.getJSONArray(i);
                String japaneseTerm = jsonEntry.getString(0);

                if(japaneseTerm.equals(word)){
                    System.out.println("Word found");
                    long end = System.nanoTime();
                    System.out.println(fileName);
                    System.out.println(word+" Search Time: "+(end - start) +" units");

                    System.out.println(japaneseTerm);
                    String hiraganaForm = jsonEntry.getString(1);
                    JSONArray meanings = jsonEntry.getJSONArray(5);

                    // Processes meanings array
                    List<String> englishDefinition = new ArrayList<>();
                    for (int j = 0; j < meanings.length(); j++) {
                        String meaning = meanings.getString(j);
                        englishDefinition.add(j+1+".)"+meaning+"\n\n");
                    }

                    // Use the extracted data as needed
                    wordTerm = new String[]{japaneseTerm, hiraganaForm, englishDefinition.toString()};
                    return wordTerm;
                }
            }
            long end = System.nanoTime();
            System.out.println(fileName);
            System.out.println(word+" Search Time: "+(end - start) +" units");

            reader.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return wordTerm;
    }

     */

    //Inserts JSONArrays into a hashmap and then retrieves the definitions from the hashmaps directly through keys
    public static String[] readDataFromFile(TextHashMap<String, JSONArray> wordMap, String word) {
        String[] wordTerm = null;

        String searchWord = word.toLowerCase();
        JSONArray jsonEntry = wordMap.get(searchWord);

        if (jsonEntry != null) {
            long start = System.nanoTime();
            String japaneseTerm = jsonEntry.getString(0);
            String hiraganaForm = jsonEntry.getString(1);
            JSONArray meanings = jsonEntry.getJSONArray(5);

            long end = System.nanoTime();
            System.out.println(searchWord+" Search Time: "+(end - start) +" units");

            List<String> englishDefinition = new ArrayList<>();
            for (int j = 0; j < meanings.length(); j++) {
                String meaning = meanings.getString(j);
                englishDefinition.add(j + 1 + ".)" + meaning + "\n\n");
            }

            wordTerm = new String[]{japaneseTerm, hiraganaForm, englishDefinition.toString()};
        }

        return wordTerm;
    }

    // Method that handles the JSONArray to HashMap insertion
    public static TextHashMap<String, JSONArray> insertDataIntoMap(List<String> fileNames) {
        TextHashMap<String, JSONArray> wordMap = new TextHashMap<>();

        for (String fileName : fileNames) {
            try {
                FileReader reader = new FileReader(fileName);
                StringBuilder jsonString = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1) {
                    jsonString.append((char) character);
                }

                JSONArray jsonArray = new JSONArray(jsonString.toString());

                long start = System.nanoTime();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray jsonEntry = jsonArray.getJSONArray(i);
                    String japaneseTerm = jsonEntry.getString(0);
                    wordMap.put(japaneseTerm, jsonEntry);
                }
                long end = System.nanoTime();
                System.out.println(fileName+"Array Objects Insertion Time: "+(end - start) +" units");

                reader.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        return wordMap;
    }

    public TextHashMap<String, JSONArray> insertDataIntoMap() {
        System.out.println("Japanese insertion begins now");

        List<String> fileNames = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            fileNames.add("src/Model/jmdict_english/term_bank_" + i + ".json");
        }

        TextHashMap<String, JSONArray> wordMap = new TextHashMap<>();

        for (String fileName : fileNames) {
            try{
                FileReader reader = new FileReader(fileName);
                StringBuilder jsonString = new StringBuilder();
                int character;
                while ((character = reader.read()) != -1) {
                    jsonString.append((char) character);
                }

                JSONArray jsonArray = new JSONArray(jsonString.toString());

                long start = System.nanoTime();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray jsonEntry = jsonArray.getJSONArray(i);
                    String japaneseTerm = jsonEntry.getString(0);
                    wordMap.put(japaneseTerm, jsonEntry);
                }
                long end = System.nanoTime();
                System.out.println(fileName+"Array Objects Insertion Time: "+(end - start) +" units");

                reader.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Japanese insertion completed");
        return wordMap;
    }

    // Function to fetch the definition with the api (For english words)
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
                // Gets the word it gets and retrieves it's array of meanings
                JSONObject firstEntry = jsonArray.getJSONObject(0);
                JSONArray meanings = firstEntry.getJSONArray("meanings");

                // Loops through the array of meanings
                for(int i = 0; i < meanings.length(); i++){
                    // Gets what part of speech the meaning fits into
                    JSONObject meaning = meanings.getJSONObject(i);
                    String speechPart = meaning.getString("partOfSpeech");
                    definitionList.add("("+speechPart+"):");

                    // Retrieves the different definitions a meaning has and loops through it if there is more than one
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

    public String[] fetchRandomWordAndDefinition(String filename) {
        String[] randomWord = null;

        try {
            String dbFilePath = executablePath + "Word Decks/"+filename;

            List<String> words = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(dbFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    words.add(line);
                }
            }

            // Pick a random word from the list
            Random random = new Random();
            int randomIndex = random.nextInt(words.size());
            String randomLine = words.get(randomIndex);

            String[] randomWordInfo = randomLine.split(",");
            System.out.println(randomWordInfo[0]);
            String wordLang = randomWordInfo[1];

            String definitions = "";
            switch (wordLang){
                case "en":
                    List<String> definitionList = fetchDefinition(randomWordInfo[0]);
                    definitions = String.join("\n\n", definitionList);
                    break;
                case "jp":
                    String[] jpWord = searchInDictionary(randomWordInfo[0]);
                    definitions = jpWord[2].replaceAll("[\\[\\],]", "");
            }

            randomWord = new String[]{randomWordInfo[0], definitions};
            return randomWord;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return randomWord;
    }
}
