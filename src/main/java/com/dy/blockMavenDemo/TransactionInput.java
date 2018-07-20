package com.dy.blockMavenDemo;

/**
 * 交易输入类
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * @date 2018/7/19
 */
public class TransactionInput {
    public String transactionOutputId;  //指向交易输出类 -> transactionId
    public TransactionOutput UTXO;  //包含所有未使用的交易输出

    public TransactionInput(String transactionOutputId){
        this.transactionOutputId = transactionOutputId;
    }
}
