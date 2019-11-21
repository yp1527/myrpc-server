package com.yp.myrpc.server.impl;

import com.yp.myrpc.server.annotation.RpcAnnotatin;
import com.yp.myrpc.server.api.IUserServer;

/**
 * 对外暴露的服务
 */
@RpcAnnotatin(value = IUserServer.class)
public class UserServerImpl implements IUserServer {
    @Override
    public void getAllUser() {
        System.out.println("服务获取所有user信息...");
    }
}
