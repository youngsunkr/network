package com.jarvisef.test;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by youngsunkr on 2015-10-26.
 */
public class Crypto
{
    private static String iv;
    private static Key keySpec;

    public static void main(String[] args)
    {
        try
        {
            String key = "hana_membership_batch_key";
            Crypto cipher = new Crypto(key);
            String originalText = "hanamemdev!08";
            String en = Encrypt( originalText );
            String de = Decrypt( en );

            System.out.println( "Original Text is " + originalText);
            System.out.println( "Encrypted Text is " + en );
            System.out.println( "Decrypted Text is " + de );
        }
        catch (Exception ex)
        {
            System.out.println("오류");
        }

    }

    /**
     * 16자리의 키값을 입력하여 객체를 생성한다.
     * @param key 암/복호화를 위한 키값
     * @throws UnsupportedEncodingException 키값의 길이가 16이하일 경우 발생
     */
    public Crypto(String key) throws UnsupportedEncodingException {
        iv = key.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if(len > keyBytes.length){
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        Crypto.keySpec = keySpec;
    }

    /**
     * AES256 으로 암호화 한다.
     * @param str 암호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String Encrypt(String str) throws GeneralSecurityException, UnsupportedEncodingException{
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
        return enStr;
    }

    /**
     * AES256으로 암호화된 txt 를 복호화한다.
     * @param str 복호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException
     * @throws UnsupportedEncodingException
     */
    public static String Decrypt(String str) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(c.doFinal(byteStr), "UTF-8");
    }


}