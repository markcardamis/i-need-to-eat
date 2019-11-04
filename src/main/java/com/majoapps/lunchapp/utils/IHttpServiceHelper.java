package com.majoapps.lunchapp.utils;

public interface IHttpServiceHelper {
    String callHttpService(String endpoint, HttpMethod method, String json) throws Exception;
}
