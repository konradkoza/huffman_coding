package p.lodz.huffman_coding;

public class Leaf  extends Node{

    private final char character;

    public Leaf(int frequency, char character) {
        super(frequency);
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }
}
