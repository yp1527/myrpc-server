package com.yp.myrpc.server.domain;

import java.io.Serializable;

/**
 * 封装的消息体
 */
public class RpcRequest implements Serializable {

    //调用的类名称
    private String className;
    //调用的方法名称
    private String methodName;
    //调用方法的参数内容
    private Object[]args;
    //版本号
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
