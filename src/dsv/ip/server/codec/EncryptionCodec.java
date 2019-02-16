package dsv.ip.server.codec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Fredrik Sander
 *
 * EncryptionCodec is responsible for providing a way to generate, store and load
 * a persistant encryption key that can be used in the application.
 */
public class EncryptionCodec {

  private static EncryptionCodec instance = new EncryptionCodec();
  private static SecretKey key;
  private String encKeyPath = "enc_key.dat";

  /**
   * Constructs a new EncryptionCodec instance and prepares the
   * SecretKey to be used within the application.
   */
  private EncryptionCodec() {
    initializeEncryptionKey();
  }

  /**
   * Fetches the static EncryptionCodec instance.
   * @return The EncryptionCodec singleton.
   */
  public static EncryptionCodec getInstance() {
    return instance;
  }

  /**
   * Fetches the SecretKey of the EncryptionCodec.
   * @return The SecretKey to be fetched.
   */
  public SecretKey getKey() {
    return key;
  }

  /**
   * Initializes the secret key for the EncryptionCodec.
   * If a previously saved key exists, it's loaded, if not, it's generated and saved.
   */
  private void initializeEncryptionKey() {
    if (Files.exists(Paths.get(encKeyPath)))
      loadExistingKey();
    else
      generateNewKey();
  }

  /**
   * Loads the currently saved SecretKey from the system's file storage.
   */
  private void loadExistingKey() {
    ObjectInputStream in = null;
    try {
      in = new ObjectInputStream(new FileInputStream(encKeyPath));
      key = (SecretKey)in.readObject();
    } catch (Exception e) { e.printStackTrace(); }
  }

  /**
   * Generates a new SecretKey and saves it on the system's file storage.
   */
  private void generateNewKey() {
    try {
      KeyGenerator generator = KeyGenerator.getInstance("Blowfish");
      generator.init(448);
      key = generator.generateKey();
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(encKeyPath));
      out.writeObject(key);
      out.close();
    } catch (Exception e) { e.printStackTrace(); };
  }
}
