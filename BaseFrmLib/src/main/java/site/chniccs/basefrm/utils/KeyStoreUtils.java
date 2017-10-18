package site.chniccs.basefrm.utils;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

/**
 * Created by chniccs on 2017/10/18 16:48.
 * @author chniccs
 * 通过密钥库进行加解密的操作工具类，请尽量在application中调用init方法
 */

@SuppressWarnings("deprecation")
public class KeyStoreUtils {
    public static String lock = "keystorelock";
    public static KeyStore mKeyStore;
    private static String mCn;
    private static String mO;
    private static String mC;

    /**
     * @param keyStoreName 密钥库名称
     * @param cn           通用名
     * @param o            组织
     * @param c            国家
     */
    public static void init(String keyStoreName, String cn, String o, String c) {
        mCn = cn;
        mO = o;
        mC = c;
        synchronized (lock) {
            if (mKeyStore == null) {
                try {
                    mKeyStore = KeyStore.getInstance(StringUtils.isEmpty(keyStoreName) ? "AndroidKeyStore" : keyStoreName);
                    mKeyStore.load(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createKey(String alias, Context context) {
        try {
            if (!mKeyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 99);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=" + mCn + ", O=" + mO))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);
                KeyPair keyPair = generator.generateKeyPair();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show(context, "创建密钥失败");
            throw new RuntimeException(new Exception("创建密钥:" + alias + "失败"));
        }
    }

    public static boolean deleteKey(String alias) {
        try {
            mKeyStore.deleteEntry(alias);
            return true;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param alias   加密的密钥名
     * @param content 加密内容
     * @param context 上下文
     * @return 为Nul 则表明出现异常
     */
    public static String encryptString(String alias, String content, Context context) {
        try {
            if (!mKeyStore.containsAlias(alias)) {
                createKey(alias, context);
            }
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(alias, null);
            PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey();

            if (content.isEmpty()) {
                ToastUtil.show(context, "加密内容不能为空");
                ;
                return null;
            }
            Cipher inCipher = null;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            } else {
                inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            }
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(content.getBytes("UTF-8"));
            cipherOutputStream.close();

            byte[] vals = outputStream.toByteArray();
            return Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param alias   key名称
     * @param content 解密内容
     * @param context 上下文
     * @return 解密后内容
     */
    public static String decryptString(String alias, String content, Context context) {
        try {
            if (!mKeyStore.containsAlias(alias)) {
                throw new RuntimeException("alias不存在");
            }
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) mKeyStore.getEntry(alias, null);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();
            Cipher output = null;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            } else {
                output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            }
            output.init(Cipher.DECRYPT_MODE, privateKey);

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(content, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }
            return new String(bytes, 0, bytes.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.show(context, "解密失败");
            return null;
        }
    }

}
