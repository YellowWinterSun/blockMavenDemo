package com.dy.simpleAlgorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 类的描述
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/9/19 8:30
 */
public class Thief {

    public static void main(String[] args){
        int[] a = {2,7,9,3,1};
        System.out.println("这些房间中能偷到的最大钱: " + func(a));
    }

    public static int func(int[] a){
        if (a.length == 1){
            return a[0];
        }

        int[] operation = new int[a.length];
        int[] f = new int[a.length];    //只有index个房间时的最大价值

        int resultIndex = -1;
        int result = -1;
        f[0] = a[0];
        f[1] = Math.max(f[0], a[1]);
        operation[0] = 1;
        operation[1] = f[1] == f[0] ? 0 : 1;

        for (int i = 2; i < a.length; i++){
            f[i] = Math.max(a[i] + f[i-2], f[i-1]);
            if (f[i] == f[i-1]){
                //当前房间不偷
                operation[i] = 0;
            }
            else{
                //当前房间偷了
                operation[i] = 1;
            }

            if (f[i] > result){
                result = f[i];  //记录当前所有组合中，最大利润的值
                resultIndex = i;    //记录达到最大利润时的房间下标
            }
        }

        //得出最有解数组好，计算其偷哪些房间才能达到最优解
        LinkedList<Integer> theifRoomIndex = new LinkedList<Integer>();
        while (resultIndex >= 0){
            if (operation[resultIndex] == 0){
                //当前房间不偷
                resultIndex --;
            }
            else {
                //当前房间偷才能达到最优解
                theifRoomIndex.addFirst(resultIndex);
                resultIndex -= 2;
            }
        }

        System.out.println("\n========= 偷哪些房间 ===============");
        //System.out.println("房间号    |      金额");
        System.out.printf("%3s\t|\t%3s\n", "房间号", "金额");
        while (!theifRoomIndex.isEmpty()){
            int index = theifRoomIndex.removeFirst();
            //System.out.println(index + "      |        " + a[index]);
            System.out.printf("%3d\t|\t%3d\n", index, a[index]);
        }
        System.out.println("============= END =================\n");

        return result;
    }
}
