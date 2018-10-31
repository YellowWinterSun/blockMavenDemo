package com.dy.leetcode;

/**
 * 通配符匹配算法
 * 用途：带通配符的URL权限校验
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/10/16 11:40
 */
public class EP44 {

    public static void main(String[] args){
        String s = "http://localhost:8080/manifestOut/1/addPost";     // 拦截的URL请求
        String p = "http://localhost:8080/manifestOut/*/addPost1";       // 当前用户拥有的某条权限URL

        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 100; ++i)
            System.out.println(isMatch(s,p));

        long time2 = System.currentTimeMillis();

        System.out.println("----------:" + (time2 - time1) + " ms");
    }

    public static boolean isMatch(String s, String p) {
        char[] str = s.toCharArray();
        char[] pattern = p.toCharArray();

        //replace multiple * with one *
        //e.g a**b --> a*b
        int writeIndex = 0;
        boolean isFirst = true;
        for (int i = 0; i < pattern.length; ++i){
            if (pattern[i] == '*'){
                if (isFirst){
                    pattern[writeIndex++] = pattern[i];
                    isFirst = false;
                }
            }
            else {
                pattern[writeIndex++] = pattern[i];
                isFirst = true;
            }
        }
        //code better faster than without this code in 10ms
        if (str.length > 0 && writeIndex > 0 && pattern[0] != '*' && pattern[0] != '?' && str[0] != pattern[0]){
            return false;
        }
        if (str.length > 0 && writeIndex > 0 && pattern[writeIndex-1] != '*' && pattern[writeIndex-1] != '?' && str[str.length-1] != pattern[writeIndex-1]){
            return false;
        }

        //begin the alo.
        boolean T[][] = new boolean[str.length+1][writeIndex+1];
        T[0][0] = true;     //because empty-string == empty-string
        if (writeIndex > 0 && pattern[0] == '*'){
            //because empty-string == *
            T[0][1] = true;
        }

        //DP deal with this problem
        for (int i = 1; i < T.length; ++i){
            for (int j = 1; j < T[0].length; ++j){
                if (pattern[j-1] == '?' || str[i-1] == pattern[j-1]){
                    T[i][j] = T[i-1][j-1];
                }
                else if (pattern[j-1] == '*'){
                    T[i][j] = T[i-1][j] || T[i][j-1];
                }
            }
        }

        return T[str.length][writeIndex];   //last one is answer
    }
}
