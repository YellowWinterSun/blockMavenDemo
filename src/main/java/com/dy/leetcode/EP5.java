package com.dy.leetcode;

/**
 * 最长回文子串
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/10/11 11:10
 */
public class EP5 {

    public static void main(String[] args){
        String str = "babad";
        System.out.println(longestPalindrome(str));
    }

    /*
    * 我的解决方法
    * O(n^3)
    * */
    public static String longestPalindrome(String s) {
        char[] array = s.toCharArray();

        int maxLength = 0;
        String result = null;

        int i = 0;
        while (i < array.length - maxLength){
            int j = i + maxLength;
            while (j < array.length){
                if (array[i] == array[j]){
                    String huiwen = isHuiWen(array, i, j);
                    result = (null == huiwen ? result : huiwen);
                    maxLength = (null == huiwen ? maxLength : huiwen.length());
                }
                j++;
            }
            i++;
        }

        return result;
    }

    public static String isHuiWen(char[] array, int i, int j){
        String str = new String(array).substring(i, j+1);

        while (i <= j){
            if (array[i] != array[j]){
                return null;
            }
            i ++;
            j --;
        }

        return str;
    }

    /*leetcode 较好的方案
    * 思想：中心扩散法
    * O(n^2)  20ms
    * */
    public String longestPalindromeBest(String s) {
        if ( s == null || "".equals(s)){
            return "";
        }
        int len = s.length();
        int start = 0 ;
        int end = 0;
        for(int i = 0 ; i< len ; i++ ){
            int len1 = expandFromCenter(s , i , i);
            int len2 = expandFromCenter(s , i , i+1);
            int tempLen = Math.max(len1 , len2);
            if ( tempLen > end - start + 1 ){
                start = i - (tempLen - 1) / 2;
                end = i + tempLen / 2;
            }
        }
        return s.substring(start ,end+1);
    }

    private int expandFromCenter(String s, int left, int right) {
        int len = s.length();
        while ( left >= 0 && right < len && s.charAt(left) == s.charAt(right)){
            left -- ;
            right ++;
        }
        return right - left -1;
    }
}
