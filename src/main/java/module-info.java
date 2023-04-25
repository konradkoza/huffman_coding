module p.lodz.huffman_coding {
    requires javafx.controls;
    requires javafx.fxml;


    opens p.lodz.huffman_coding to javafx.fxml;
    exports p.lodz.huffman_coding;
}