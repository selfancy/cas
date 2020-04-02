package com.haofei.cas.utils;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * @author lizhi
 */
public class SendHttpUtil {
    private static Logger logger = LoggerFactory.getLogger(SendHttpUtil.class);


    /**
     * @param requestString 发送的参数
     * @param pathUrl       发送的链接
     * @return error_code 0成功，1服务器错误，2未知错误，3参数非法，4无效body，5未知命令
     */
    public static String sendPost(String requestString, String pathUrl) {
        return sendPost(requestString,pathUrl,null);
    }


    public static String sendPost(String requestString, String pathUrl, Map<String,String> headers) {
        logger.info(pathUrl + "==" + requestString);
        JSONObject json = new JSONObject();
        // 建立连接
        try {
            URL url = new URL(pathUrl);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            // 设置连接属性
            httpUrlConnection.setDoOutput(true);
            // 使用 URL 连接进行输出
            httpUrlConnection.setDoInput(true);
            // 使用 URL 连接进行输入
            httpUrlConnection.setUseCaches(false);
            // 忽略缓存
            httpUrlConnection.setRequestMethod("POST");
            // 设置URL请求方法
            httpUrlConnection.setRequestProperty("CHARSET", "UTF-8");
            httpUrlConnection.setConnectTimeout(5000);
            httpUrlConnection.setReadTimeout(5000);
            // 设置请求属性
            // 获得数据字节数据，请求数据流的编码，必须和下面服务器端处理请求流的编码一致
            byte[] requestStringBytes = requestString.getBytes("UTF-8");
            httpUrlConnection.setRequestProperty("Content-length", "" + requestStringBytes.length);
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            // 维持长连接
            httpUrlConnection.setRequestProperty("Charset", "UTF-8");
            if(headers!=null&&!headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpUrlConnection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }


            // 建立输出流，并写入数据
            OutputStream outputStream = httpUrlConnection.getOutputStream();
            outputStream.write(requestStringBytes);
            outputStream.close();
            // 获得响应状态
            int responseCode = httpUrlConnection.getResponseCode();
            String readLine;
            // 连接成功
            if (HttpURLConnection.HTTP_OK == responseCode) {
                // 当正确响应时处理数据
                StringBuilder sb = new StringBuilder();

                BufferedReader responseReader;
                // 处理响应流，必须与服务器响应流输出的编码一致
                responseReader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), StandardCharsets.UTF_8));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine);
                }
                responseReader.close();
                String result = sb.toString();
                String jsonStr = "{";
                // 处理返回的参数
                if (!"".equals(result) && result.contains(jsonStr)) {
                    json = new JSONObject(result);
                } else {
                    return result;
                }
            }
            json.put("HTTP_CODE", responseCode);
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！" + e);
            return null;
        }
        logger.info(String.valueOf(json));
        return json.toString();
    }

}


