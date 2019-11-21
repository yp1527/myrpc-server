package com.yp.myrpc.server.impl;

import com.yp.myrpc.server.annotation.RpcAnnotatin;
import com.yp.myrpc.server.api.IHelloServer;

/**
 * 对外暴露的服务 版本为v1.0
 */
@RpcAnnotatin(value = IHelloServer.class,version = "v1.0")
public class HelloServerImpl implements IHelloServer {
    @Override
    public String sayHello(String content) {
        System.out.println("v1.0 服务端收到消息:"+content);
        return "v1.0 hello:"+content;
    }
}
