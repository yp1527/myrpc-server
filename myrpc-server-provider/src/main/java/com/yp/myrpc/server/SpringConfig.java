package com.yp.myrpc.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.yp.myrpc.server")
public class SpringConfig {

    /**
     * 把RpcProxyServer托管到spring容器中
     * @return
     */
    @Bean(name="rpcProxyServer")
    public RpcProxyServer getRpcProxyServer(){
        return new RpcProxyServer(8080);
    }
}