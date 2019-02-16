package dsv.ip.server.codec;

import javax.crypto.Cipher;
import java.util.Base64;

/**
 * @author Fredrik Sander
 *
 * DecryptionHandler is responsible for performing decryption operations in the system.
 */
public class DecryptionHandler {

  private String algorithm = "Blowfish/ECB/PKCS5Padding";

  public byte[] decrypt(byte[] content) {
    try {
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.DECRYPT_MODE, EncryptionCodec.getInstance().getKey());
      return cipher.doFinal(content);
    } catch(Exception e ) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Decrypts an encrypted string.
   * @param content The encrypted string to decrypt.
   * @return The decrypted string.
   */
  public String decrypt(String content) {
    byte[] decoded = Base64.getDecoder().decode(content);
    byte[] decrypted = decrypt(decoded);
    return new String(decrypted);
  }
}
