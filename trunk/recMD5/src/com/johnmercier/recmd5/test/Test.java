/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnmercier.recmd5.test;

import com.johnmercier.recmd5.MD5MessageDigest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class Test {

    public static void main(String... args) {
        MessageDigest expected = null;
        MD5MessageDigest test = null;
        try {
            expected = MessageDigest.getInstance("md5");
            test = new MD5MessageDigest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

        String message = "Hello world!";

        expected.update(message.getBytes());
        String s = new BigInteger(1, expected.digest()).toString(16);
        if (s.length() == 31) {
            s = "0" + s;
        }
        test.digest(message.getBytes());
        System.out.println(s);
        System.out.println(test.getState().toString());
    }
}
