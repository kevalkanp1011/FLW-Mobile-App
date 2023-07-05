package org.piramalswasthya.sakhi.crypt;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Base64;

public class AESUtil {

    public static String encrypt(byte[] iv,String plaintext, byte[] key) throws Exception {
//        byte[] iv = new byte[16]; // Initialization vector (IV)

        BufferedBlockCipher cipher = new BufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
        cipher.init(true, new ParametersWithIV(new KeyParameter(key), iv));

        byte[] input = plaintext.getBytes("UTF-8");
        byte[] output = new byte[cipher.getOutputSize(input.length)];
        int encryptedLength = cipher.processBytes(input, 0, input.length, output, 0);
        encryptedLength += cipher.doFinal(output, encryptedLength);

        return new String(Base64.encode(output, 0, encryptedLength), "UTF-8");
    }

    public static String decrypt(String ciphertext, byte[] key) throws Exception {
        byte[] iv = new byte[16]; // Initialization vector (IV)

        BufferedBlockCipher cipher = new BufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
        cipher.init(false, new ParametersWithIV(new KeyParameter(key), iv));

        byte[] input = Base64.decode(ciphertext);
        byte[] output = new byte[cipher.getOutputSize(input.length)];
        int decryptedLength = cipher.processBytes(input, 0, input.length, output, 0);
        decryptedLength += cipher.doFinal(output, decryptedLength);

        return new String(output, 0, decryptedLength, "UTF-8");
    }
}

