package p.lodz.huffman_coding;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private final FileChooser fileChooser = new FileChooser();

    private String encodedMessage;

    private String originalMessage;

    private String receivedDecodedMessage;

    private String codingDictionary;

    private HuffmanCoding huffmanCoding;

    @FXML
    private TextField portTextField;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button encodeMessageButton;

    @FXML
    private TextArea dictionaryText;

    @FXML
    private TextArea encodedMessageText;

    @FXML
    private TextArea originalMessageText;

    @FXML
    private Button readFileButton;

    @FXML
    private Button receiveNessageButton;

    @FXML
    private TextArea receivedMessageText;

    @FXML
    private Button sendMessageButton;

    @FXML
    private Button saveFileButton;

    @FXML
    private Button saveEncodedMessageButton;

    @FXML
    private Button saveDictionaryButton;


    @FXML
    void saveDictionary(ActionEvent event) {
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("ALL", "*.*"));
        File file = fileChooser.showSaveDialog(null);
        if(huffmanCoding != null) {
            FileReader.saveToFile(huffmanCoding.getCodesString(), file.getAbsolutePath());
        } else {
            FileReader.saveBytesFile(dictionaryText.getText().getBytes(),file.getAbsolutePath());
        }
    }

    @FXML
    void encodeMessage(ActionEvent event) {
        if(originalMessage != null) {
            huffmanCoding = new HuffmanCoding(originalMessage);
            encodedMessage = huffmanCoding.encode();
            encodedMessageText.setText(encodedMessage);
            dictionaryText.setText(huffmanCoding.getCodesString());
        } else {
            System.out.println("brak wiadomosci");
        }
    }

    @FXML
    void readMessageFromFile(ActionEvent event) {
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("ALL", "*.*"));
        File file = fileChooser.showOpenDialog(null);
        originalMessage = FileReader.readMessage(file.getAbsolutePath());
        originalMessageText.setText(originalMessage);
    }

    @FXML
    void receiveMessage(ActionEvent event) {
        try(ReceivingClient receivingClient = new ReceivingClient();){
            receivingClient.start(Integer.parseInt(portTextField.getText()));
            byte[] bytes = receivingClient.getReceivedBytes();
            java.util.Map<Character, String> dictionary = (Map<Character, String>) receivingClient.getReceivedObject();
            HuffmanCoding huffmanDecoding = new HuffmanCoding(dictionary);
            String receivedText = Converter.removePaddingFromBitString(Converter.byteArrayToBitString(bytes));
            encodedMessageText.setText(receivedText);
            dictionaryText.setText(huffmanDecoding.getCodesString());
            receivedDecodedMessage = huffmanDecoding.decodeWithDictionary(receivedText);
            receivedMessageText.setText(receivedDecodedMessage);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



    @FXML
    void saveMessageToFile(ActionEvent event) {
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("TXT files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        FileReader.saveToFile(receivedDecodedMessage, file.getAbsolutePath());

    }


    @FXML
    void sendMessage(ActionEvent event) {
        if(ipTextField.getText().matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$")) {
            try (SendingClient sendingClient = new SendingClient();
            ) {
                sendingClient.start(Integer.parseInt(portTextField.getText()), ipTextField.getText());
                String padded = Converter.addPaddingToBitString(encodedMessage);
                byte[] bytes = Converter.bitStringToByteArray(padded);
                String response = sendingClient.sendObject(bytes);
                if (response.equals("ok")) {
                    sendingClient.sendObject(huffmanCoding.getHuffmanDictionary());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("zly adres");
        }
    }

    @FXML
    void saveEncodedMessage(ActionEvent event) {
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("ALL", "*.*"));
        File file = fileChooser.showSaveDialog(null);
        String padded = Converter.addPaddingToBitString(encodedMessage);
        byte[] bytes = Converter.bitStringToByteArray(padded);
        FileReader.saveBytesFile(bytes, file.getAbsolutePath());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        portTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(newValue.matches("^[0-9]{0,5}$") || newValue.equals(""))){
                    portTextField.setText(oldValue);
                }
            }
        });
    }
}
