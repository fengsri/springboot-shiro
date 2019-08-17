package com.feng.shiro.util;

import org.apache.shiro.crypto.hash.Md5Hash;

public class MD5Test {
    public static void main(String args[]){
        String str = "123456";
        String md5 = new Md5Hash(str).toString();
        System.out.println(md5);

        String str2 = "hello";
        String salt2 = "123";
        String md52 = new Md5Hash(str2, salt2).toString();
        String ss = EncryptionUtil.getMd5HashSalt(32);
        System.out.println(ss);
    }
}
