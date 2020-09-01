package com.fspt.practice.net;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 提供网络请求的操作
 */

public class HttpHelper {

    private final String TAG = "HttpHelper";
    private static HttpHelper instance = null;
    private final static String ERROR = "连接服务器失败";

    /**
     * Post 请求
     *
     * @param path    请求路径
     * @param strBody 数据参数json格式
     * @return 请求成功返回数据信息  失败返回null
     */
    public String httpPost(String path, String strBody) {
        try {
            //指定提交数据的路径，建立连接
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为Post请求
            conn.setRequestMethod("POST");
            //post的请求是把数据以流的方式写给了服务器
            //指定请求的输出模式
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置超时时间 10s
            conn.setConnectTimeout(5 * 1000);
            //请求格式json格式
            conn.setRequestProperty("Content-Type",
                    "application/json");
            conn.connect();

            //写数据
            if (!"".equals(strBody)) {
                OutputStream out = conn.getOutputStream();
                out.write(strBody.getBytes());
                out.flush();
                out.close();
            }

            //获取返回的请求码
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                String result = readStream(is);
                Log.i(TAG, result);
                //断开连接
                conn.disconnect();
                return result;
            } else {
                InputStream is = conn.getInputStream();
                String result = readStream(is);
                Log.w(TAG, result);
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            Log.w(TAG, "httpPost MalformedURLException " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.w(TAG, "httpPost IO " + e.getMessage());
            e.printStackTrace();
        }
        return ERROR;
    }

    //读取流中的数据
    private String readStream(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        is.close();
        String result = baos.toString("utf-8");//utf-8
        baos.close();
        return result;
    }


}
