package com.dy.blockMavenDemo;

/**
 * 类的描述
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/7/27 10:35
 */
public class ThreadTest {

    public static void main(String[] args){
        MyThread1 t1 = new MyThread1();
        MyThread2 t2 = new MyThread2();

        t1.start();
        t2.start();
    }

    public static void toRun(String hi){
        for (int i = 0; i < 1000; i++){
            System.out.println(hi + i);
        }
    }
}

class MyThread1 extends Thread{

    public void run(){
        ThreadTest.toRun("lo===========");
    }
}

class MyThread2 extends Thread{

    public void run(){
        ThreadTest.toRun("hi---");
    }
}