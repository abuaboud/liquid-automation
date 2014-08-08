package org.liquidbot.bot.client.security.encryption;

import org.liquidbot.bot.Configuration;
import org.liquidbot.bot.script.api.util.Random;
import org.liquidbot.bot.ui.login.misc.User;
import sun.security.krb5.Config;

import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private final String IV;
    private final String ENCRYPTION_KEY;
    private final String KEYBOARD;
    private final String SPECIAL_CHARACTER;

    public AES() {
        User user = Configuration.getInstance().getUser();
        IV = user.getHash().substring(0, 16);
        ENCRYPTION_KEY = user.getHash().substring(16, 32);
        SPECIAL_CHARACTER = user.getDisplayName().charAt(0) + "";
        KEYBOARD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&".replace(SPECIAL_CHARACTER, "");
    }

    public AES(String IV, String ENCRYPTION_KEY) {
        this.IV = IV;
        this.ENCRYPTION_KEY = ENCRYPTION_KEY;
        this.SPECIAL_CHARACTER = ENCRYPTION_KEY.charAt(0) + "";
        this.KEYBOARD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&".replace(SPECIAL_CHARACTER, "");
    }

    private byte[] StringToBytes(String encryptedText) {
        encryptedText = encryptedText.replaceAll(SPECIAL_CHARACTER, "-");
        String byteLine = "";
        for (int i = 0; i < encryptedText.length(); i++) {
            if (KEYBOARD.contains(encryptedText.charAt(i) + "")) {
                byteLine = byteLine + " ";
            } else {
                byteLine = byteLine + encryptedText.charAt(i);
            }
        }
        String[] bytesInText = byteLine.split(" ");
        byte[] bytes = new byte[bytesInText.length];
        for (int byteIndex = 0; byteIndex < bytes.length; byteIndex++) {
            bytes[byteIndex] = Byte.parseByte(bytesInText[byteIndex]);
        }
        return bytes;
    }

    private String bytesToString(byte[] bytes) {
        String a = "";
        for (byte d : bytes)
            a = a + d + KEYBOARD.charAt(Random.nextInt(0, KEYBOARD.length()));
        a = a.replaceAll("-", SPECIAL_CHARACTER);
        return a;
    }

    private static String fix(String s) {
        if (s.length() > 16) {
            for (int i = 0; i < 32; i++) {
                if (s.length() < 32) {
                    s = s + "\0";
                }
            }
        } else {
            for (int i = 0; i < 16; i++) {
                if (s.length() < 16) {
                    s = s + "\0";
                }
            }
        }
        return s;
    }

    public String encrypt(String plainText) {
        try {
            plainText = fix(plainText);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
            return bytesToString(cipher.doFinal(plainText.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String decrypt(String cipherText) {
        try {
            byte[] cipherBytes = StringToBytes(cipherText);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(ENCRYPTION_KEY.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
            return new String(cipher.doFinal(cipherBytes), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}