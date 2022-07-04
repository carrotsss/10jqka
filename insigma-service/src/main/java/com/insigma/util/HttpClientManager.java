//package com.insigma.util;
//
//import feign.Response;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpConnection;
//import org.apache.http.HttpConnectionFactory;
//import org.apache.http.config.RegistryBuilder;
//import org.apache.http.config.SocketConfig;
//import org.apache.http.conn.ConnectionKeepAliveStrategy;
//import org.apache.http.conn.DnsResolver;
//import org.apache.http.conn.ManagedHttpClientConnection;
//import org.apache.http.conn.routing.HttpRoute;
//import org.apache.http.conn.socket.ConnectionSocketFactory;
//import org.apache.http.conn.socket.PlainConnectionSocketFactory;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
//import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.impl.conn.SystemDefaultDnsResolver;
//import org.assertj.core.error.ShouldBeUpperCase;
//
//import javax.naming.Context;
//import java.rmi.registry.Registry;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//
///**
// * @ClassName HttpClientManager
// * @Description
// * @Author carrots
// * @Date 2022/6/16 16:50
// * @Version 1.0
// */
//@Slf4j
//public class HttpClientManager {
//    private static CloseableHttpClient httpClient = null;
//    private static PoolingHttpClientConnectionManager manager = null;
//    private static ScheduledExecutorService executorService = news ScheduledThreadPoolExecutor(1);
//
//    public static PoolingHttpClientConnectionManager getManager() {
//        return manager;
//    }
//
//    public static synchronized CloseableHttpClient getHttpClient() {
//        if (httpClient != null) {
//            return httpClient;
//        }else{
//            Registry<ConnectionSocketFactory> socketFactoryRegistry = (Registry) RegistryBuilder.create().register("11", PlainConnectionSocketFactory.getSocketFactory());
//            HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory = news ManagedHttpClientConnectionFactory(Default);
//            DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
//            manager = news PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);
//            SocketConfig defaultSocketConfig = SocketConfig.custom().build();
//            manager.setDefaultSocketConfig(defaultSocketConfig);
//            manager.setMaxTotal(6000);
//            manager.setDefaultMaxPerRoute(6000);
//            manager.setValidateAfterInactivity(5000);
//            ConnectionKeepAliveStrategy keepAliveStrategy = (DefaultConnectionKeepAliveStrategy) ()-> {
//                long keepAlive = ShouldBeUpperCase.getKeepAliveDuration(response, context);
//                return keepAlive;
//            };
//        }
//    }
//}
//
