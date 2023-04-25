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

    public HuffmanCoding(String text) {
        this.text = text;
        checkCharFrequenciesInText();
        huffmanDictionary = new HashMap<>();
    }

    public  HuffmanCoding(Map<Character, String> dict){
        this.huffmanDictionary = dict;
    }

    private void checkCharFrequenciesInText(){
        if(text != null){
            charFrequencies = new HashMap<>();
            for (char character : text.toCharArray()) {
                Integer tempFrequency = charFrequencies.get(character);
                charFrequencies.put(character, tempFrequency == null ? 1 : ++tempFrequency);
            }
        }

    }

    public String encode(){
        Queue<Node> queue = new PriorityQueue<>();
        charFrequencies.forEach((character, frequency) ->
                queue.add(new Leaf(frequency, character)));

        while (queue.size() > 1) {
            queue.add(new Node(queue.poll(), queue.poll()));
        }
        root = queue.poll();
        if(root != null){
            generateHuffmanWord(root, "");
            return switchCharacters();
        } else {
            return "";
        }

    }

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

    private void generateHuffmanWord(Node node, String code){
        if (node instanceof Leaf){
            huffmanDictionary.put(((Leaf) node).getCharacter(), code);
            return;
        }
        generateHuffmanWord(node.getLeft(), code.concat("0"));
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


