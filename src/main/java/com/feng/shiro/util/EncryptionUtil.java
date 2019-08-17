package com.feng.shiro.util;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.util.StringUtils;

import java.util.Random;


/**
 * @Description 加密解密
 * @Author chenlinghong
 * @Date 2018/12/1 23:13
 **/
public final class EncryptionUtil {

    private static String salt = "fh15,.>?>..'dsfg345ig435fggdfdgfd6546;';':'.'.345fds30R%&*Y&GUGi;';]::";

    /**
     * CC SHA-1加密算法
     *
     * @param plaintext 明文
     * @return 密文
     */
    public static String ccSHA1(String plaintext) {
        String base = plaintext + "/" + salt;
        return DigestUtils.sha1Hex(base);
    }

    /**
     * CC MD5加密
     *
     * @param plaintext 明文
     * @return 密文
     */
    public static String ccMD5(String plaintext) {
        String base = plaintext + "/" + salt;
        return DigestUtils.md5Hex(base.getBytes());
    }


    /**
     * 使用BouncyCastle实现的Base64进行加密
     *
     * @param plaintext 明文
     * @return 密文
     */
    public static String encodeByBase64(String plaintext) {
        if (StringUtils.isEmpty(plaintext)) {
            return null;
        }
        byte[] encodeBytes = Base64.encode(plaintext.getBytes());
        return new String(encodeBytes);
    }

    /**
     * 使用BouncyCastle实现的Base64进行解密
     *
     * @param ciphertext 解密
     * @return
     */
    public static String decodeByBase64(String ciphertext) {
        byte[] decodeBytes = Base64.decode(ciphertext);
        return new String(decodeBytes);
    }

    /**
     * 与客户端约定好的对称加密算法【算法、密钥】
     *
     * @param plain 明文
     * @return 密文
     */
    public static String encodeClient(String plain) {
        //TODO
        return plain;
    }

    /**
     * 与客户端约定好的对称加密算法【算法、密钥】
     *
     * @param cipher 密文
     * @return 明文
     */
    public static String decodeClient(String cipher) {
        //TODO
        return cipher;
    }


    /**
     * 注册用户时，获取随机参数的盐值
     * @return
     */
    public static String getMd5HashSalt(int length){
        String str="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //2.  由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //3.  长度为几就循环几次
        for(int i=0; i<length; ++i){
            //从62个的数字或字母中选择
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    /**
     * 注册用户时使用该方法
     * @param password
     * @return
     */
    public static String md5Hash(String password,String salt){
        String md5Password = new Md5Hash(password, salt,2).toString();
        return md5Password;
    }



}
