package com.dy;

import com.google.gson.Gson;
import com.sun.deploy.util.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 类的描述
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/11/2 8:27
 */
public class AppMain2 {


    public static void main(String[] args) throws InterruptedException {
        A.B b1 = new A.B();
        b1.a = "line1-a";
        b1.b = "line1-b";

        A.B b2 = new A.B();
        b2.a = "line2-a";
        b2.b = "line2-b";

        A a = new A();
        a.name = "table1";
        a.listB = new ArrayList<>();
        a.listB.add(b1);
        a.listB.add(b2);

        String json = new Gson().toJson(a);

        System.out.println(json);

        Thread.sleep(2000);

        A afterA = new Gson().fromJson(json, A.class);

        System.out.println("afterA:" + afterA.toString());

        List<String> listStr = new ArrayList<>();
        listStr.add("hi");
        listStr.add(new String(""));
        listStr.add("hello");

        System.out.println(StringUtils.join(listStr, ","));
    }
}

class A  {
    String name;
    List<B> listB;

    @Override
    public String toString() {
        return "A{" +
                "name='" + name + '\'' +
                ", listB=" + listB +
                '}';
    }

    public static class B  {
        String a;
        String b;

        public B(){}

        @Override
        public String toString() {
            return "B{" +
                    "a='" + a + '\'' +
                    ", b='" + b + '\'' +
                    '}';
        }
    }
}