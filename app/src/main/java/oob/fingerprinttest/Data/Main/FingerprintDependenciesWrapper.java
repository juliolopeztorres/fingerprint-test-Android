package oob.fingerprinttest.Data.Main;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Base64;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import oob.fingerprinttest.Framework.ApplicationContext;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintDependenciesWrapper {
    private static final String KEY_ALIAS = "Fingerprint Test Bundle";
    private static final String KEYSTORE = "AndroidKeyStore";

    private static KeyguardManager keyguardManager;
    private static FingerprintManagerCompat fingerprintManager;

    private static KeyStore keyStore;
    private static Cipher cipher;

    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        fingerprintManager = FingerprintManagerCompat.from(context);
        sharedPreferences = context.getSharedPreferences(ApplicationContext.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static FingerprintManagerCompat getFingerprintManager() {
        return fingerprintManager;
    }

    public static String encryptString(Cipher cipher, String textToEncrypt) {
        try {
            return Base64.encodeToString(
                    cipher.doFinal(textToEncrypt.getBytes()),
                    Base64.NO_WRAP
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decryptString(Cipher cipher, String cipherText) {
        try {
            return new String(
                    cipher.doFinal(
                            Base64.decode(cipherText, Base64.NO_WRAP)
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static FingerprintManagerCompat.CryptoObject getCryptoObjectForEncrypting() {
        if (!getCipherForEncrypting()) {
            return null;
        }

        try {
            return new FingerprintManagerCompat.CryptoObject(cipher);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static FingerprintManagerCompat.CryptoObject getCryptoObjectForDecrypting() {
        if (!getCipherForDecrypting()) {
            return null;
        }

        try {
            return new FingerprintManagerCompat.CryptoObject(cipher);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static boolean getKeyStore() {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE);
            keyStore.load(null);
            return true;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean createNewKey(boolean forceCreate) {
        try {
            if (forceCreate) {
                keyStore.deleteEntry(KEY_ALIAS);
            }

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE);

                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setUserAuthenticationRequired(true)
                        .build()
                );

                keyGenerator.generateKey();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean getCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);

            return true;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean initCipher(int mode) {
        try {
            keyStore.load(null);
            SecretKey keySpec = (SecretKey) keyStore.getKey(KEY_ALIAS, null);

            if (mode == Cipher.ENCRYPT_MODE) {
                cipher.init(mode, keySpec);

                sharedPreferences.edit().putString(
                        ApplicationContext.PREFERENCES_IV_KEY,
                        Base64.encodeToString(
                                cipher.getIV(),
                                Base64.NO_WRAP
                        )
                ).apply();
            } else {
                byte[] iv = Base64.decode(sharedPreferences.getString(ApplicationContext.PREFERENCES_IV_KEY, ""), Base64.NO_WRAP);
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(mode, keySpec, ivSpec);
            }

            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            e.printStackTrace();
            createNewKey(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean getCipherForEncrypting() {
        return prepareAndGetCipher() && initCipher(Cipher.ENCRYPT_MODE);
    }

    private static boolean getCipherForDecrypting() {
        return prepareAndGetCipher() && initCipher(Cipher.DECRYPT_MODE);

    }

    private static boolean prepareAndGetCipher() {
        return getKeyStore() && createNewKey(false) && getCipher();
    }
}
