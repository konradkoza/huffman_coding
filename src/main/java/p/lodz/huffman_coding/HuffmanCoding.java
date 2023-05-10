package p.lodz.huffman_coding;

import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;


public class HuffmanCoding {
    private Node root;
    private String text;
    private Map<Character, Integer> charFrequencies;
    private final Map<Character, String> huffmanDictionary;

    //konstruktor do kodowania tekstu
    public HuffmanCoding(String text) {
        this.text = text;
        checkCharFrequenciesInText();
        huffmanDictionary = new HashMap<>();
    }

    //konstruktor do dekodowania na podstawie podanego słownika
    public  HuffmanCoding(Map<Character, String> dict){
        this.huffmanDictionary = dict;
    }

    //sprawdzenie częstotliwości występowania poszczególnych znaków
    private void checkCharFrequenciesInText(){
        if(text != null){
            charFrequencies = new HashMap<>();
            for (char character : text.toCharArray()) {
                Integer tempFrequency = charFrequencies.get(character);
                charFrequencies.put(character, tempFrequency == null ? 1 : ++tempFrequency);
            }
        }

    }

    //kodowanie wiadomości
    public String encode(){
        Queue<Node> queue = new PriorityQueue<>();
        //dodaj liście ze znakami do kolejki
        charFrequencies.forEach((character, frequency) ->
                queue.add(new Leaf(frequency, character)));
        //tworzenie drzewa na podstawie częstotliwości występowania poszczególnych znaków
        //funkcja pobiera kolejne dwa węzły bądż liście z kolejki, która układa elementy w kolejności
        //od najmniejszej częstotliwości występowania znaku dla liści, bądź sumy częstotliwości potomoków węzła do największej częstotliwości
        while (queue.size() > 1) {
            queue.add(new Node(queue.poll(), queue.poll()));
        }
        root = queue.poll();
        if(root != null){
            //generowanie słownika znaków na podstawie struktury drzewa
            generateHuffmanWord(root, "");
            //podmiana znaków na zakodowane na podstawie stworzonego słownika
            return switchCharacters();
        } else {
            return "";
        }

    }

    //podmiana znaków w oryginalnej wiadomoości na znaki ze słownika
    private String switchCharacters() {
        if(text != null) {
            StringBuilder builder = new StringBuilder();
            for (char character : text.toCharArray()) {
                builder.append(huffmanDictionary.get(character));
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    //rekurencyjna funkcja do uzupełniania słownika znaków
    private void generateHuffmanWord(Node node, String code){
        if (node instanceof Leaf){
            huffmanDictionary.put(((Leaf) node).getCharacter(), code);
            return;
        }
        //dla węzłą lewego dodaj 0 do zakodowanego znaku
        generateHuffmanWord(node.getLeft(), code.concat("0"));
        //dla węzłą prawego dodaj 1 do zakodowanego znaku
        generateHuffmanWord(node.getRight(), code.concat("1"));
    }

    public String decode(String encoded){
        StringBuilder builder = new StringBuilder();
        Node currentNode = root;
        for (char character: encoded.toCharArray()) {
            if (character == '0'){
                currentNode = currentNode.getLeft();
            } else if (character == '1'){
                currentNode = currentNode.getRight();
            }
            if (currentNode instanceof Leaf){
                builder.append(((Leaf) currentNode).getCharacter());
                currentNode = root;
            }
            
        }
        return builder.toString();
    }

    //funkcja dekodująca zakodowaną wiadomość na podstawie podanego słownika znaków
    public String decodeWithDictionary(String encoded){
        StringBuilder decoded = new StringBuilder();
        String word = "";
        for (char character: encoded.toCharArray()) {
            word =  word.concat(Character.toString(character));
            if(huffmanDictionary.containsValue(word)){
                for (Map.Entry<Character, String> entry : huffmanDictionary.entrySet()) {
                    if (entry.getValue().equals(word)) {
                        decoded.append(entry.getKey());
                        word = "";
                    }
                }
            }
        }
        return decoded.toString();
    }

    public String getCodesString() {
        StringBuilder stringBuilder = new StringBuilder();
        huffmanDictionary.forEach((character, code) ->
                stringBuilder.append(character + ": " + code + "\n")
        );
        return stringBuilder.toString();
    }

    public java.util.Map<Character, String> getHuffmanDictionary() {
        return huffmanDictionary;
    }
}


