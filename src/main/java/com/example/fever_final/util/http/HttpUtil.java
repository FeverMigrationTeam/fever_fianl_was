package com.example.fever_final.util.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.json.GsonJsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpUtil {


        /** HttpURLConnection POST 방식 */
        public static String callApi(String targetUrl, Map<String, Object> requestMap, String type) {

            String response = "";

            try {

                URL url = new URL(targetUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST"); // 전송 방식
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setConnectTimeout(5000); // 연결 타임아웃 설정(5초)
                conn.setReadTimeout(5000); // 읽기 타임아웃 설정(5초)
                conn.setDoOutput(true);	// URL 연결을 출력용으로 사용(true)

                String requestBody = getJsonStringFromMap(requestMap);
//                System.out.println("requestBody:" + requestBody);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                bw.write(requestBody);
                bw.flush();
                bw.close();

                System.out.println("getContentType():" + conn.getContentType()); // 응답 콘텐츠 유형 구하기
                System.out.println("getResponseCode():"    + conn.getResponseCode()); // 응답 코드 구하기
                System.out.println("getResponseMessage():" + conn.getResponseMessage()); // 응답 메시지 구하기

                Charset charset = Charset.forName("UTF-8");
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));

                String inputLine;
                StringBuffer sb = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                br.close();

                response = sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        /** Map을 jsonString으로 변환 */
        @SuppressWarnings("unchecked")
        public static String getJsonStringFromMap(Map<String, Object> map) throws JSONException {

            JSONObject json = new JSONObject();

            for(Map.Entry<String, Object> entry : map.entrySet()) {

                String key = entry.getKey();
                Object value = entry.getValue();

                json.put(key, value);
            }

            return json.toString();
        }
}
