package com.majoapps.lunchapp.utils;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServiceHelper implements IHttpServiceHelper
{
    private static final int HTTP_REQUEST_TIMEOUT = 60000;

    @Override
    public String callHttpService(String url, HttpMethod method, String json) throws Exception {

        String result;
        URL obj = new URL(url);

        HttpsURLConnection request = (HttpsURLConnection) obj.openConnection();
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        request.setSSLSocketFactory(sslContext.getSocketFactory());
        request.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
        request.setReadTimeout(HTTP_REQUEST_TIMEOUT);
        String httpMethodString  = method.toString();
        request.setRequestMethod(httpMethodString);
        request.setUseCaches (false);

        if (httpMethodString.equals("POST") || httpMethodString.equals("PUT"))
        {
            request.setDoOutput(true);
            request.setRequestProperty("Content-Length","" + json.getBytes().length);
            try
            {
                DataOutputStream wr = new DataOutputStream (request.getOutputStream());
                wr.writeBytes (json);
                wr.flush ();
                wr.close ();
            }catch (Exception ex) {
                log.error("Exception ", ex);
                throw new Exception(ex);
            }
        }

        try
        {
            request.connect();

            int httpCode = request.getResponseCode();
            BufferedReader rd;

            if (httpCode == 200 || httpCode == 201) {
                rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
            } else if (request.getErrorStream() != null) {
                rd = new BufferedReader(new InputStreamReader(request.getErrorStream()));
                log.warn("HTTP Error Response : {}", rd);
            } else {
                log.error("IllegalStateException : {} ", httpCode);
                throw new IllegalStateException(httpCode + " : Connection closed, no ErrorStream");
            }

            String line;
            StringBuilder response = new StringBuilder();

            while((line = rd.readLine()) != null) {
                response.append(line).append('\n');
            }
            rd.close();
            result = response.toString();

            if(!(httpCode == 200 || httpCode == 201)) {
                log.error("IllegalStateException {} : {}", httpCode, request.getResponseMessage());
                throw new IllegalStateException(httpCode + " " + request.getResponseMessage());
            }

        } catch (SocketTimeoutException e){
            log.error("SocketTimeoutException ", e);
            throw new Exception(e);
        }
        finally
        {
            request.disconnect();
        }
        return result;
    }
}



