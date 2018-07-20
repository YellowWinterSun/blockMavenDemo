package com.dy.blockMavenDemo;

import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

public class Sha256Util {
    /**
     * 采用Sha256进行数字加密
     * @param input
     * @return
     */
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * 应用ECDSA进行数字签名
     * @param privateKey 签名者的私钥
     * @param input 内容
     * @return
     */
    public static byte[] applyECDSASig(PrivateKey privateKey, String input){
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return output;
    }

    /**
     * 应用ECDSA进行数字签名的验证
     * @param publicKey 要验证的签名的预期公钥
     * @param data 内容
     * @param signature 数字签名
     * @return
     */
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature){
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String getMerkleRoot(ArrayList<Transaction> transactions){
        int count = transactions.size();
        ArrayList<String> previousTreeLayer = new ArrayList();
        for (Transaction transaction : transactions){
            previousTreeLayer.add(transaction.transactionId);
        }
        ArrayList<String> treeLayer = previousTreeLayer;
        while (count > 1){
            treeLayer = new ArrayList<String>();
            for (int i = 1; i < previousTreeLayer.size(); i++){
                treeLayer.add(applySha256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }
}
