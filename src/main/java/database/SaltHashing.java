package database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Utilities for hashing passwords
 */
public class SaltHashing {
    private static MessageDigest sha256;
    public synchronized static String saltSHA256(String salt, String password) {
        String fullPassword = salt + password;
        try {
            StringBuilder builder = new StringBuilder();
            builder.append(salt);
            builder.append(password);
            String encryptedCredentials = encryptionIterator(builder.toString());

            return encryptedCredentials;
        }
        catch (Exception e) {e.printStackTrace();}

        return "";
    }
    private static String encryptionIterator(String content) throws NoSuchAlgorithmException {
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
            byte[] passBytes = (content).getBytes();
            sha256.reset();
            byte[] digested = sha256.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {ex.printStackTrace();}
        return "";
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public static String toHex(byte[] bytes) {
        HexFormat hexFormat = HexFormat.of();
        return hexFormat.formatHex(bytes);
    }

    public static byte[] toByteArray(String str) {
        HexFormat hexFormat = HexFormat.of();
        return hexFormat.parseHex(str);
    }

    public static byte[] fromHex(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) {
        String password = "1111";
        String salt = "7c4e324002c571b09b3002f5c97c7c92";
        String encrypt = saltSHA256(salt,password);
        System.out.println("Your Password Is '" + encrypt + "'");
    }
}
