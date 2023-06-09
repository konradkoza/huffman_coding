package p.lodz.huffman_coding;

import javafx.scene.control.Alert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileReader {

    public static String readMessage(String fileName){
        byte[] result;

        try(FileInputStream fis = new FileInputStream(fileName)) {
            result = fis.readAllBytes();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        return new String(result, StandardCharsets.UTF_8);
    }

    public static void saveToFile(String text, String fileName)  {
        try{
            Files.writeString(Paths.get(fileName), text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public void saveBytesFile(byte[] dane, String fileName) {
        try(FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(dane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static public void saveDictionaryToFile(Map<Character, String> dict, String fileName){
        try(FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream os = new ObjectOutputStream(fos)){
            os.writeObject(dict);

        } catch (IOException e) {
            System.out.println("Problem z zapisem słownika");
        }
    }

}
