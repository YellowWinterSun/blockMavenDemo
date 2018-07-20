package com.dy.blockMavenDemo;

public class App {
    public static void main(String[] args){
        System.out.println("hello world");

        Block genesisBlock = new Block("no.1 block", "0");
        System.out.println("Hash for no.1 is : " + genesisBlock.hash);

        Block secondBlock = new Block("no.2 block", genesisBlock.hash);
        System.out.println("Hash for no.2 is : " + secondBlock.hash);

        Block thirdBlock = new Block("no.3 block", secondBlock.hash);
        System.out.println("Hash for no.3 is : " + thirdBlock.hash);
    }
}
