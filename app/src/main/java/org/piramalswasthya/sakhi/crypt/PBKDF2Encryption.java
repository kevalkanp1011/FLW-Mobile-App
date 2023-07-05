package org.piramalswasthya.sakhi.crypt;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

public class PBKDF2Encryption {

    public static byte[] generateKey(char[] passphrase, byte[] salt) {
        int iterations = 1989; // Number of iterations
        int keyLength = 256; // Key length in bits

        PKCS5S2ParametersGenerator generator = new PKCS5S2ParametersGenerator();
        generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(passphrase), salt, iterations);
        KeyParameter keyParameter = (KeyParameter) generator.generateDerivedMacParameters(keyLength);

        return keyParameter.getKey();
    }
}
