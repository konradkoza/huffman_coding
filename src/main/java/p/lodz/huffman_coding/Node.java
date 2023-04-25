package p.lodz.huffman_coding;

public class Node implements Comparable<Node>{

    private final int frequency;
    private Node left;
    private Node right;

    public Node( Node left, Node right) {
        this.frequency = left.getFrequency() + right.getFrequency();
        this.left = left;
        this.right = right;
    }

    public Node(int frequency){
        this.frequency = frequency;
        left = null;
        right = null;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public int compareTo(Node node) {
        return  Integer.compare(this.frequency, node.getFrequency());
    }

    public int getFrequency() {
        return frequency;
    }
}