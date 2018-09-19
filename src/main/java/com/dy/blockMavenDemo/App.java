package com.dy.blockMavenDemo;

import java.math.BigDecimal;

public class App {
    public static void main(String[] args){
        System.out.println("hello world");

        Block genesisBlock = new Block("no.1 block", "0");
        System.out.println("Hash for no.1 is : " + genesisBlock.hash);

        Block secondBlock = new Block("no.2 block", genesisBlock.hash);
        System.out.println("Hash for no.2 is : " + secondBlock.hash);

        Block thirdBlock = new Block("no.3 block", secondBlock.hash);
        System.out.println("Hash for no.3 is : " + thirdBlock.hash);

        Double d1 = 5.2D;
        Double d2 = 4.2222D;
        Double d3 = new BigDecimal(d1).add(new BigDecimal(d2)).doubleValue();
        System.out.println(d3);
        System.out.println(d3.compareTo(Double.parseDouble("9.4222")) > 0);
    }
}
