package com.dy.blockMavenDemo;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * 交易类
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * @date 2018/7/19
 */
public class Transaction {
    public String transactionId;    //交易的hash编号
    public PublicKey sender;    //付款人的公钥
    public PublicKey reciepient;    //收款人的公钥
    public float value; //转移金额
    public byte[] signature;    //数字签名，防止其他人从我们的钱包中发送资金

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    //多少个交易已经被创建
    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs){
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    //计算交易的hash值
    private String calulateHash(){
        sequence++;
        return Sha256Util.applySha256(Sha256Util.getStringFromKey(sender)
                + Sha256Util.getStringFromKey(reciepient)
                + Float.toString(value)
                + sequence
        );
    }

    public void generateSignature(PrivateKey privateKey){
        String data = Sha256Util.getStringFromKey(sender) + Sha256Util.getStringFromKey(reciepient) + Float.toString(value);
        signature = Sha256Util.applyECDSASig(privateKey, data);
    }

    public boolean verifiySignature(){
        String data = Sha256Util.getStringFromKey(sender) + Sha256Util.getStringFromKey(reciepient) + Float.toString(value);
        return Sha256Util.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction(){
        //验证签名
        if (verifiySignature() == false){
            System.out.println("安全验证不通过，交易签名发生了篡改");
            return false;
        }

        //收集交易的输入（必须注意的是输入是未使用的）
        for (TransactionInput i : inputs){
            i.UTXO = NoobChain.UTXOs.get(i.transactionOutputId);
        }

        //检查交易是否是有效的
        if (getInputsValue() < NoobChain.minimumTransaction){
            System.out.println("交易输入太小:" + getInputsValue());
            return false;
        }

        //创建交易输出
        float leftOver = getInputsValue() - value;  //获得输入的剩余金额
        transactionId = calulateHash();
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId));  //发送金额给收款人
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));   //把剩余的金额返回给付款人
        //把输出增加到未使用的列表中
        for (TransactionOutput o : outputs){
            NoobChain.UTXOs.put(o.id, o);
        }

        //把已使用的交易输入从UTXO中移除
        for (TransactionInput i : inputs){
            if (i.UTXO == null){
                continue;
            }
            NoobChain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    //返回金额
    public float getInputsValue(){
        float total = 0;
        for (TransactionInput i : inputs){
            if (i.UTXO == null){
                continue;
            }
            total += i.UTXO.value;
        }
        return total;
    }

    //返回输出的总和
    public float getOutputsValue(){
        float total = 0;
        for (TransactionOutput o : outputs){
            total += o.value;
        }
        return total;
    }

}
