package com.uec.quartz.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * @Description 提供通过http协议调用外部接口的方法，基于java.net.HttpURLConnection
 * @author sunqian
 * @date 2018年5月11日 上午10:32:28
 */
@Slf4j
public class HttpUtil {

	public static String post(String path, String jsonRequestBody) {
		String result = "";
		String encoding = "UTF-8";
		HttpURLConnection conn = null;
		OutputStream outStream = null;
		BufferedReader in = null;
		try {
			URL url = new URL(path);
			byte[] data = jsonRequestBody.getBytes(encoding);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Type", "application/json; charset=" + encoding);
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));
			conn.setConnectTimeout(60 * 1000 * 3);
			conn.setReadTimeout(60 * 1000 * 3);
			outStream = conn.getOutputStream();
			outStream.write(data);
			outStream.flush();
			outStream.close();
			log.debug(conn.getResponseCode() + ""); // 响应代码 200表示成功
			int responseCode = conn.getResponseCode();
			if (responseCode >= 400) {
				in = new BufferedReader(new InputStreamReader(conn.getErrorStream(), encoding));
				String msg = readMsgFromBufferedReader(in);
				result = msg;
				log.debug(String.format("请求[%s]返回异常状态码[%s]及信息[%s]", conn.getURL(), responseCode, msg));
			} else {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
				String msg = readMsgFromBufferedReader(in);
				result = msg;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.debug(e.getMessage());
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					log.debug(e.getMessage());
				}
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return result;
	}

	public static String get(String urlPath) {
		String result = null;
		try {
			URL url=new URL(urlPath);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(30000);
            urlConnection.setReadTimeout(30000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int code=urlConnection.getResponseCode();
            if (code==200) {
                InputStream inputStream=urlConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer buffer=new StringBuffer();
                while ((line=bufferedReader.readLine())!=null) {
                    buffer.append(line);
                }
                result = buffer.toString();
            }
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	private static String readMsgFromBufferedReader(BufferedReader in) throws IOException {
		String msg = "";
		String line;
		while ((line = in.readLine()) != null) {
			msg += line;
		}
		return msg;
	}

}
