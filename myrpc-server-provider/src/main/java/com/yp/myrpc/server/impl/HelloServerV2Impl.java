package com.yp.myrpc.server.impl;

import com.yp.myrpc.server.annotation.RpcAnnotatin;
import com.yp.myrpc.server.api.IHelloServer;
/**
 * 对外暴露的服务 版本为v2.0
 */
@RpcAnnotatin(value = IHelloServer.class,version = "v2.0")
public class HelloServerV2Impl implements IHelloServer {
    @Override
    public String sayHello(String content) {
        System.out.println("v2.0 服务端收到消息:"+content);
        return "v2.0 hello:"+content;
    }
}
