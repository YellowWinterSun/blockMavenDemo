package com.dy.leetcode;

import java.util.*;

/**
 * 类的描述
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/9/19 11:46
 */
public class Soluation {

    public static void main(String[] args){
        int[] nums = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> list = threeSum(nums);
        System.out.println("ok");
    }


    public static List<List<Integer>> threeSum(int[] nums) {

        List<List<Integer>> res = new ArrayList<List<Integer>>();

        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++){
            if (i > 1 && nums[i] == nums[i-1])
                continue;

            int low = i+1;
            int high = nums.length-1;
            int sum = 0 - nums[i];

            while (low < high){
                if (nums[low] + nums[high] == sum){
                    res.add(Arrays.asList(nums[low], nums[high], nums[i]));
                    while (low < high && nums[low] == nums[low + 1]){
                        low++;
                    }
                    while (low < high && nums[high] == nums[high - 1]){
                        high --;
                    }
                    low ++;
                    high --;
                }
                else if (nums[low] + nums[high] < sum){
                    low++;
                }
                else{
                    high --;
                }
            }
        }

        return res;
    }

}
