package com.dy.blockMavenDemo;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 钱包
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * @date 2018/7/19
 */
public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    //钱包自身的UTXO
    public HashMap<String, TransactionOutput> UTXOs = new HashMap();

    public Wallet(){
        generateKeyPair();
    }

    /**
     * 采用椭圆曲线加密算法
     * 生成公钥密钥对
     */
    public void generateKeyPair(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        } catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    //返回余额并保存钱包自身的UTXO
    public float getBalance(){
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : NoobChain.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)){
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    //创建并返回这个钱包的一个新交易
    public Transaction sendFunds(PublicKey _recipient, float value){
        if (getBalance() < value){
            //搜索并检查金额
            System.out.println("不够钱进行交易");
            return null;
        }

        //创建输入列表
        ArrayList<TransactionInput> inputs = new ArrayList();
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value) {
                break;
            }
        }
        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);
        for (TransactionInput input : inputs){
            UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;

    }
}
