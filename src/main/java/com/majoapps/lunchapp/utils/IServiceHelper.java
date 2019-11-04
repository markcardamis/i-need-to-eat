package com.majoapps.lunchapp.utils;

public interface IServiceHelper {
    String callHttpService(String endpoint, HttpMethod method, String json) throws Exception;
}
