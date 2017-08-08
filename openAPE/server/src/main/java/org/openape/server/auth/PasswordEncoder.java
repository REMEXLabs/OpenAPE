package org.openape.server.auth;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Manage secure password hashes using PBKDF2 with HMACSHA1.
 */

public class PasswordEncoder {

    /**
     * Secures a password generating a random hash.
     *
     * @param password
     *            Plain text password to be encoded
     * @return The password hash
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String encode(final String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final int iterations = 1000;
        final char[] chars = password.toCharArray();
        final byte[] salt = PasswordEncoder.getSalt();
        final PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        final SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        final byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + PasswordEncoder.toHex(salt) + ":" + PasswordEncoder.toHex(hash);
    }

    private static byte[] fromHex(final String hex) throws NoSuchAlgorithmException {
        final byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, (2 * i) + 2), 16);
        }
        return bytes;
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        final SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        final byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * Checks if a plain text password matches a hashed one.
     *
     * @param plainPassword
     *            The plain password
     * @param encodedPassword
     *            The encoded password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean matches(final String plainPassword, final String encodedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        final String[] parts = encodedPassword.split(":");
        final int iterations = Integer.parseInt(parts[0]);
        final byte[] salt = PasswordEncoder.fromHex(parts[1]);
        final byte[] hash = PasswordEncoder.fromHex(parts[2]);
        final PBEKeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, iterations, hash.length * 8);
        final SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        final byte[] testHash = skf.generateSecret(spec).getEncoded();
        int diff = hash.length ^ testHash.length;
        for (int i = 0; (i < hash.length) && (i < testHash.length); i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static String toHex(final byte[] array) throws NoSuchAlgorithmException {
        final BigInteger bi = new BigInteger(1, array);
        final String hex = bi.toString(16);
        final int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

}
