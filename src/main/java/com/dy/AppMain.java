package com.dy;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 类的描述
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/10/12 10:25
 */
public class AppMain {
    public static void main(String[] args){
        AppMain appMain = new AppMain();

        int[] nums = {2,3,1,2,4,3};
        System.out.println(appMain.minSubArrayLen(7, nums));

        LinkedList<Integer> queue = new LinkedList<Integer>();

    }

    public int minSubArrayLen(int s, int[] nums) {

        if (nums.length == 0){
            return 0;
        }

        int i = 0;
        int j = 0;
        int sum = nums[0];
        int result = Integer.MAX_VALUE;

        while (i < nums.length && j < nums.length){
//             if (j-i+1 > result){

//             }
            if (sum < s){
                if (j+1 >= nums.length){
                    break;
                }
                sum += nums[++j];
            }
            else{
                result = Math.min(result, j-i+1);
                if (i+1 > j){
                    return 1;
                }
                sum -= nums[i++];
            }
        }

        if (result == Integer.MAX_VALUE){
            return 0;
        }
        return result;
    }
}


