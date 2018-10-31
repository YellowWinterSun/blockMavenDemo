package com.dy.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 高性能序列化工具 Kyro (建议用于Redis中的值读写)
 *
 * @author HuangDongYang<huangdy @ pvc123.com>
 * Create on 2018/9/26 10:20
 */
public class KryoUtil {

    private static Kryo kryo = new Kryo();

    public static String objectToByte(Object obj) throws Exception {

        //序列化大小，2M和65M。序列化对象大于65M会报错
        Output output = new Output(2048, 65536);

        try {
            kryo.writeClassAndObject(output, obj);
            byte[] resultBs = output.toBytes();

            return new String(resultBs, "ISO8859-1");
        } catch (Exception e ){
            e.printStackTrace();
            throw new Exception("Kyro序列化失败");
        } finally {
            output.close();
        }

    }

    public static Object toObject(String str) throws Exception {

        byte[] bs = str.getBytes("ISO8859-1");

        Input input = new Input(bs);
        try {
            Object obj = kryo.readClassAndObject(input);

            return obj;
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("Kyro序列化失败");
        } finally {
            input.close();
        }
    }
}
