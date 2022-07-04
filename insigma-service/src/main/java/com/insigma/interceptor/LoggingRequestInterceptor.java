package com.insigma.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName LoggingRequestInterceptor
 * @Description
 * @Author carrots
 * @Date 2022/7/4 11:14
 * @Version 1.0
 */
@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws UnsupportedEncodingException {
        log.info("URI:{}", request.getURI());
        log.info("Method:{} ", request.getMethod());
        log.info("body: {}", new String(body, "UTF-8"));
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inpustStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inpustStringBuilder.append(line);
            inpustStringBuilder.append("\n");
            line = bufferedReader.readLine();
        }
        log.info(response.getStatusCode().toString());
        log.info(inpustStringBuilder.toString());
    }
}
