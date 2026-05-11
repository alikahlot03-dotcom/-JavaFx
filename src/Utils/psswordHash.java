/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author hp
 */
public class psswordHash {
    
    
   static public String hashPassword(String password) throws NoSuchAlgorithmException {
        // 1. استدعاء خوارزمية MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        // 2. تحويل كلمة المرور إلى Bytes ومعالجتها
        md.update(password.getBytes());
        byte[] digest = md.digest();
        
        // 3. تحويل الـ Bytes إلى نص (Hexadecimal) لسهولة تخزينه في ملف txt
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString(); // هذا هو النص المشفر الذي ستخزنه
        
    }
    }

    
