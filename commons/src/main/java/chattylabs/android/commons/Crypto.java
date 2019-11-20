package chattylabs.android.commons;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {
    public static String encryptMD5(String... keys) {
        StringBuilder s = new StringBuilder();
        for (String key : keys) s.append("+").append(key);
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.toString().getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            return hexString.toString();
        } catch (NoSuchAlgorithmException ignored) {
            return s.toString();
        }
    }
}
