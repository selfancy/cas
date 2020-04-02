package com.haofei.cas.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。
 *
 * @author lizhi
 */
public class RSAUtils {

    private static final BouncyCastleProvider BOUNCY_CASTLE_PROVIDER = new BouncyCastleProvider();

    /**
     * * 生成密钥对 *
     *
     * @return KeyPair *
     * @throws Exception 异常
     */
    public static KeyPair generateKeyPair() throws Exception {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",
                    BOUNCY_CASTLE_PROVIDER);
            // 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
            final int keysize = 1024;
            keyPairGen.initialize(keysize, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            return keyPair;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    /**
     * * 生成公钥 *
     *
     * @param modulus        *
     * @param publicExponent *
     * @return RSAPublicKey *
     * @throws Exception 异常
     */
    public static RSAPublicKey generateRsaPublicKey(byte[] modulus,
                                                    byte[] publicExponent) throws Exception {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA",
                    BOUNCY_CASTLE_PROVIDER);
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
                modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * * 生成私钥 *
     *
     * @param modulus         *
     * @param privateExponent *
     * @return RSAPrivateKey *
     * @throws Exception 异常
     */
    public static RSAPrivateKey generateRsaPrivateKey(byte[] modulus, byte[] privateExponent) throws Exception {
        KeyFactory keyFac;
        try {
            keyFac = KeyFactory.getInstance("RSA",
                    BOUNCY_CASTLE_PROVIDER);
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }

        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(
                modulus), new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * * 加密 *
     *
     * @param pk   公钥
     * @param data 待加密的明文数据 *
     * @return 加密后的数据 *
     * @throws Exception 加密异常
     */
    private static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA",
                    BOUNCY_CASTLE_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            int blockSize = cipher.getBlockSize();
            // 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
            // 加密块大小为127
            // byte,加密后为128个byte;因此共有2个加密块，第一个127
            // byte第二个为1个byte
            int outputSize = cipher.getOutputSize(data.length);
            // 获得加密块加密后块大小
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1
                    : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize) {
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i
                            * outputSize);
                } else {
                    cipher.doFinal(data, i * blockSize, data.length - i
                            * blockSize, raw, i * outputSize);
                }
                /* 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
                // ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
                // OutputSize所以只好用dofinal方法。
                */
                i++;
            }
            return raw;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * * 解密 *
     *
     * @param pk  公钥
     * @param raw 已经加密的数据 *
     * @return 解密后的明文 *
     * @throws Exception 异常
     */
    public static byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA",
                BOUNCY_CASTLE_PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, pk);
        int blockSize = cipher.getBlockSize();
        ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
        int j = 0;

        while (raw.length - j * blockSize > 0) {
            bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
            j++;
        }
        return bout.toByteArray();

    }

}
