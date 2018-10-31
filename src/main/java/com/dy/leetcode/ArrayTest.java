package com.dy.leetcode;

/**
 * 数组 练习
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/9/29 15:29
 */
public class ArrayTest {

    public static void main(String[] args){
        ArrayTest arrayTest = new ArrayTest();

        int[] nums = {1,1,1,2,2,3,3,3,3,4,5,6,6,7,7,9};
        System.out.println(arrayTest.removeDuplicates(nums));
        for (int i = 0; i < nums.length; i++){
            System.out.print(nums[i] + ", ");

        }
    }

    public int removeDuplicates(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length; i++){
            if (nums[i] != nums[res]){
                nums[++res] = nums[i];
            }
        }

        return res+1;
    }
}
