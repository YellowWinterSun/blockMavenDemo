package com.dy.blockMavenDemo;

import java.util.ArrayList;

public class Block {
    public String hash;
    public String preHash;
    public String data;
    private long timeStamp;
    private int nonce;  //随机数

    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Block(String data, String preHash){
        this.data = data;
        this.preHash = preHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public Block(String preHash){
        this.preHash = preHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        String calculatedhash = Sha256Util.applySha256(preHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
        return calculatedhash;
    }

    public void mineBlock(int difficulty){
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)){
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!!:" + hash);
    }

    //在区块中增加交易
    public boolean addTransaction(Transaction transaction){
        //处理交易并检查是否有效，创世纪区块将被忽略
        if (transaction == null){
            return false;
        }

        if (preHash != "0"){
            if (transaction.processTransaction() != true){
                System.out.println("交易fail to process");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("交易成功加入到区块中");
        return true;
    }
}
