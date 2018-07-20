package com.dy.blockMavenDemo;

import com.google.gson.GsonBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class NoobChain {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 3;

    public static Wallet walletA;
    public static Wallet walletB;

    //未使用的输出集合
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public static float minimumTransaction = 0.1f;
    public static Transaction genesisTransaction;

    public static void main(String[] args){
        /*demo1*/
//        blockchain.add(new Block("no.1", "0"));
//        blockchain.get(0).mineBlock(difficulty);
//
//        for (int i = 0;i < 3; i++){
//            blockchain.add(new Block("no." + (i + 2), blockchain.get(i).hash));
//            blockchain.get(i + 1).mineBlock(difficulty);
//        }
//
//        System.out.println("\nBlockchain is Valid: " + isChainValid());
//
//        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
//        System.out.println(blockchainJson);

        /*demo2*/
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        //钱包
//        walletA = new Wallet();
//        walletB = new Wallet();
//        //测试公钥和私钥
//        System.out.println("钱包A的私钥和公钥：");
//        System.out.println(Sha256Util.getStringFromKey(walletA.privateKey));
//        System.out.println(Sha256Util.getStringFromKey(walletA.publicKey));
//
//        System.out.println("钱包B的私钥和公钥：");
//        System.out.println(Sha256Util.getStringFromKey(walletB.privateKey));
//        System.out.println(Sha256Util.getStringFromKey(walletB.publicKey));
//
//        //创建一个交易，A给B转移了5块钱
//        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
//        //用walletA的私钥进行数字签名
//        transaction.generateSignature(walletA.privateKey);
//
//        System.out.println("钱包A的私钥对交易进行数字签名后：" + transaction.signature.toString());
//
//        //用钱包A的公钥验证签名
//        System.out.println("\n 是否是钱包A发起的交易");
//        System.out.println(transaction.verifiySignature());

        /*demo3*/
        Security.addProvider(new BouncyCastleProvider());
        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();
        //创建创世纪交易，将100个货币发送给walletA
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);  //数字签名
        genesisTransaction.transactionId = "0"; //默认设置创世纪交易的输入为0
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId));
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //保存我们第一个交易到UTXO列表中

        System.out.println("Creating and Mining Genesis block...");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //测试
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(block1);

        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());
        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (120) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 120f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block3 = new Block(blockchain.get(blockchain.size() - 1).hash);
        System.out.println("B -> A  $30");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 30f));
        addBlock(block3);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block4 = new Block(block3.hash);
        System.out.println("A -> B  $60");
        block4.addTransaction(walletA.sendFunds(walletB.publicKey, 60f));
        addBlock(block4);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        isChainValid();

        //开始动态操作钱包
        while (1 == 1) {
            Scanner scan = new Scanner(System.in);
            System.out.println("请选择你的付款钱包：（1）钱包A （2）钱包B (3)退出 (4)恶意篡改区块链");
            int selectWallet = scan.nextInt();
            if (selectWallet == 3) {
                System.out.println("---------- EXIT ---------");
                isChainValid();
                System.exit(0);
            }
            if (selectWallet == 4) {
                blockchain.get(blockchain.size() - 1).transactions.get(0).value = 4.4f;
                isChainValid();
                continue;
            }
            scan.nextLine();
            System.out.print("请填写要转移的金额:");
            float selectMoney = scan.nextFloat();
            scan.nextLine();

            Block nowBlock = new Block(blockchain.get(blockchain.size() - 1).hash);
            if (selectWallet == 1) {
                System.out.println("A -> B $" + selectMoney);
                nowBlock.addTransaction(walletA.sendFunds(walletB.publicKey, selectMoney));
                addBlock(nowBlock);
            } else {
                System.out.println("B -> A $" + selectMoney);
                nowBlock.addTransaction(walletB.sendFunds(walletA.publicKey, selectMoney));
                addBlock(nowBlock);
            }
            System.out.println("\nWalletA's balance is: " + walletA.getBalance());
            System.out.println("WalletB's balance is: " + walletB.getBalance());
            isChainValid();
        }

    }

    public static boolean isChainValid(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //循环遍历区块链进行hash检查
        for (int i = 1; i < blockchain.size(); i++){
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("当前hash不对");
                return false;
            }

            if (!previousBlock.hash.equals(currentBlock.preHash)){
                System.out.println("前一个hash不对");
                return false;
            }

            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)){
                System.out.print("这个区块不是我的");
                return false;
            }

            //遍历区块链的交易
            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.transactions.size(); t++){
                Transaction currentTransaction = currentBlock.transactions.get(t);
                if (!currentTransaction.verifiySignature()){
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }

                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()){
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for (TransactionInput input : currentTransaction.inputs){
                    tempOutput = tempUTXOs.get(input.transactionOutputId);
                    if (tempOutput == null){
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if (input.UTXO.value != tempOutput.value){
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for (TransactionOutput output : currentTransaction.outputs){
                    tempUTXOs.put(output.id, output);
                }

                if (currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient){
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }

                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }
            }
        }

        System.out.println("Blockchain is valid");

        return true;
    }

    /**
     * 向区块链中增加区块
     * @param newBlock
     */
    public static void addBlock(Block newBlock){
        newBlock.mineBlock(difficulty);
        if (newBlock.transactions.isEmpty()){
            return;
        }
        blockchain.add(newBlock);
    }
}
