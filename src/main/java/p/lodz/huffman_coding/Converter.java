package p.lodz.huffman_coding;

public class Converter {

    public static String addPaddingToBitString(String inputBitString) {
        int paddingLength = 8 - (inputBitString.length() % 8);
        if (paddingLength == 8) {
            return inputBitString; // input string is already padded
        }
        StringBuilder paddedBitStringBuilder = new StringBuilder(inputBitString);
        paddedBitStringBuilder.append("0"); // add a final '0' bit to mark the end of the padding
        for (int i = 0; i < paddingLength - 1; i++) {
            paddedBitStringBuilder.append("1"); // pad with '1' bits
        }
        return paddedBitStringBuilder.toString();
    }

    public static String removePaddingFromBitString(String receivedBitString) {
        int paddingStartIndex = receivedBitString.lastIndexOf("01") + 2;
        if (paddingStartIndex == 1) {
            return receivedBitString; // no padding found
        }
        return receivedBitString.substring(0, paddingStartIndex - 2);
    }

    public static byte[] bitStringToByteArray(String bitString) {
        if (bitString == null || bitString.length() % 8 != 0) {
            return null; // input string is invalid or not padded to byte boundary
        }
        byte[] byteArray = new byte[bitString.length() / 8];
        for (int i = 0; i < byteArray.length; i++) {
            String byteString = bitString.substring(i * 8, (i + 1) * 8);
            byteArray[i] = (byte) Integer.parseInt(byteString, 2);
        }
        return byteArray;
    }

    public static String byteArrayToBitString(byte[] byteArray) {
        StringBuilder bitStringBuilder = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            String byteString = Integer.toBinaryString(byteArray[i] & 0xFF);
            while (byteString.length() < 8) {
                byteString = "0" + byteString; // pad with leading zeroes if necessary
            }
            bitStringBuilder.append(byteString);
        }
        return bitStringBuilder.toString();
    }
}
