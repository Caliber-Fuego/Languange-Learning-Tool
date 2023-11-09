package Model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class WordSegmenter {
    private TrieNode root;
    private TextHashMap<String, JSONArray> wordMap;
    private int currentFileIndex = 0; // Keep track of the current file index

    private static wordBase wb = new wordBase();
    private List<String> fileSet; // Stores the current set of files in memory

    public WordSegmenter() {
        root = new TrieNode();
        fileSet = new ArrayList<>(); // Initialize the file set
        loadNextFileSet(); // Load the initial set of files
    }

    private void loadNextFileSet() {
        int startIndex = currentFileIndex;
        int endIndex = Math.min(startIndex + 32, 32); // Loads files in sets (can be controlled)

        for (int i = startIndex; i < endIndex; i++) {
            fileSet.add("src/Model/jmdict_english/term_bank_" + (i + 1) + ".json");
        }

        TextHashMap<String, JSONArray> newWordMap = wb.insertDataIntoMap(fileSet);

        if (wordMap == null) {
            wordMap = newWordMap;
        } else {
            // Merge the newWordMap into the existing wordMap
            wordMap.putAll(newWordMap);
        }

        currentFileIndex = endIndex; // Update the current file index
        fileSet.clear();
    }

    class TrieNode {
        private TextHashMap<Character, TrieNode> children;
        private boolean isEndOfWord;

        public TrieNode() {
            children = new TextHashMap<>();
            isEndOfWord = false;
        }

        public TextHashMap<Character, TrieNode> getChildren() {
            return children;
        }

        public void setEndOfWord(boolean endOfWord) {
            isEndOfWord = endOfWord;
        }

        public boolean isEndOfWord() {
            return isEndOfWord;
        }

        public TrieNode computeIfAbsent(char key) {
            TrieNode child = children.get(key);
            if (child == null) {
                child = new TrieNode();
                children.put(key, child);
            }
            return child;
        }

        public boolean containsKey(char key) {
            return children.get(key) != null;
        }
    }

    public void insertWord(String word) {
        TrieNode currentNode = root;
        for (char ch : word.toCharArray()) {
            currentNode = currentNode.computeIfAbsent(ch);
        }
        currentNode.setEndOfWord(true);
    }

    public List<String> segmentText(String text) {
        List<String> segments = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = start;
            TrieNode currentNode = root;

            while (end < text.length() && currentNode.containsKey(text.charAt(end))) {
                currentNode = currentNode.getChildren().get(text.charAt(end));
                end++;

                if (currentNode.isEndOfWord()) {
                    segments.add(text.substring(start, end));
                }
            }

            if (end == start) {
                // No valid word, treat character as a segment.
                segments.add(text.substring(start, start + 1));
                start++;
            } else {
                start = end;
            }
        }
        return segments;
    }

    public List<String> segmentTextWithKanji(String text) {
        List<String> segments = new ArrayList<>();
        int start = 0;

        while (start < text.length()) {
            int end = findValidEnd(text, start);

            if (end == -1) {
                // No valid word found, treat character as a segment.
                segments.add(text.substring(start, start + 1));
                start++;
            } else {
                segments.add(text.substring(start, end));
                start = end;
            }
        }

        return segments;
    }

    private int findValidEnd(String text, int start) {
        TrieNode currentNode = root;
        int end = start;

        while (end < text.length() && currentNode.containsKey(text.charAt(end))) {
            currentNode = currentNode.getChildren().get(text.charAt(end));
            end++;

            if (currentNode.isEndOfWord()) {
                return end;
            }
        }

        return findValidEndFromMap(text, start);
    }

    private int findValidEndFromMap(String text, int start) {
        for (int i = text.length(); i > start; i--) {
            String wordToCheck = text.substring(start, i);
            if (wordMap.containsKey(wordToCheck)) {
                return i;
            }
        }

        /*
        // Word not found in the current map, try loading the next file set and check again
        loadNextFileSet();

        for (int i = text.length(); i > start; i--) {
            String wordToCheck = text.substring(start, i);
            if (wordMap.containsKey(wordToCheck)) {
                return i;
            }
        }
        */

        return -1;
    }
}
