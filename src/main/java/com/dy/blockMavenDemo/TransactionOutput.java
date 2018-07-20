package com.dy.blockMavenDemo;

import java.security.PublicKey;

/**
 * 交易输出类
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * @date 2018/7/19
 */
public class TransactionOutput {
    public String id;
    public PublicKey reciepient;    //持有者公钥
    public float value; //持有者金额
    public String parentTransactionId;  //交易编号

    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId){
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = Sha256Util.applySha256(Sha256Util.getStringFromKey(reciepient) + Float.toString(value) + parentTransactionId);

    }

    //用来验证是否属于你
    public boolean isMine(PublicKey publicKey){
        return (publicKey == reciepient);
    }
}
