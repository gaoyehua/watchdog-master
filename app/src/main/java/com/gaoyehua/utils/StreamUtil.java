package com.gaoyehua.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by 高业华 on 2016/7/16.
 */
public class StreamUtil {
    /*
    将流信息转化为字符串

     */
    public static String parserStreamUtil(InputStream in) throws IOException {
        //字节流，读取流
        BufferedReader br =new BufferedReader(new InputStreamReader(in));
        //写入流
        StringWriter sw = new StringWriter();
        //读写操作
        //数据缓存
        String str =null;
        while ((str= br.readLine()) !=null){
            //写入操作
            sw.write(str);

        }
        //关流
        br.close();
        sw.close();
        return sw.toString();
    }
}
