package dsv.ip.server.codec;

import javax.crypto.Cipher;
import java.util.Base64;

/**
 * @author Fredrik Sander
 *
 * Responsible for different encryption tasks.
 */
public class EncryptionHandler {

  // The algorithm that is used during encryption.
  private String algorithm = "Blowfish/ECB/PKCS5Padding";

  /**
   * Encrypts a byte array with the established encryption algorithm.
   * @param content The content to encrypt.
   * @return Returns the encrypted byte array.
   */
  public byte[] encrypt(byte[] content) {
    try {
     Cipher cipher = Cipher.getInstance(algorithm);
     cipher.init(Cipher.ENCRYPT_MODE, EncryptionCodec.getInstance().getKey());
     return cipher.doFinal(content);
    } catch (Exception e) { e.printStackTrace(); }

   return null;
  }

  /**
   * Encrypts a string.
   * @param original The string content to encrypt.
   * @return The encrypted string.
   */
  public String encrypt(String original) {
    byte[] decoded = original.getBytes();
    byte[] encrypted = encrypt(decoded);
    return Base64.getEncoder().encodeToString(encrypted);
  }
}
