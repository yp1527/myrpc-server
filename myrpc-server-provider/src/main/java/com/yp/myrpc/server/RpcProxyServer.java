package com.yp.myrpc.server;

import com.yp.myrpc.server.annotation.RpcAnnotatin;
import com.yp.myrpc.server.register.RegistryCenterWithZk;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1.当容器启动以后 遍历容器中所有的对象 把RpcAnnotatin注解标记的类存储起来
 * 2.当对象初始化以后 开启socket监听 接收客户端的请求
 * 3.收到客户端请求后 从输入流中解析出RpcRequest
 * 4.根据RpcRequest 调用相应的服务类 并把结果返回给客户端
 */
@Component
public class RpcProxyServer implements ApplicationContextAware,InitializingBean {

    //处理请求的线程池
    private ExecutorService executorService=  Executors.newCachedThreadPool();
    //发布服务类的容器
    private Map<String,Object> serverData=new HashMap();

    private RegistryCenterWithZk registry=new RegistryCenterWithZk();
    //端口号
    private int port;

    public RpcProxyServer( int port) {
        this.port = port;
    }

    /**
     * 当对象初始化后 进行服务的发布过程
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            ServerSocket serverSocket=new ServerSocket(port);
            System.out.println("服务器已经启动...");
            for(Map.Entry<String, Object> entry : serverData.entrySet()){
                String mapKey = entry.getKey();
                Object mapValue = entry.getValue();
                System.out.println("服务名称:"+mapKey+"  服务对象:"+mapValue);
            }
            while (true){
                Socket socket=serverSocket.accept();
                executorService.submit(new ProcessorHandler(socket,serverData));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 容器初始化后得到所有要发布的类
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> serviceBeanMap=applicationContext.getBeansWithAnnotation(RpcAnnotatin.class);
        if(!serviceBeanMap.isEmpty()){
            for(Object servcieBean:serviceBeanMap.values()){
                //拿到注解
                RpcAnnotatin rpcService=servcieBean.getClass().getAnnotation((RpcAnnotatin.class));
                String serviceName=rpcService.value().getName();//拿到接口类定义名称
                String version=rpcService.version(); //拿到版本号
                if(!StringUtils.isEmpty(version)){
                    serviceName+="-"+version;
                }
                serverData.put(serviceName,servcieBean);
                //把服务地址注册到zookeeper上
                registry.registry(serviceName,getAddress()+":"+port);
            }
        }
    }

    /**
     * 获取本机ip地址
     * @return
     */
    private String getAddress(){
        InetAddress inetAddress=null;
        try {
            inetAddress=InetAddress.getLocalHost();
        }catch (Exception e){
            e.printStackTrace();
        }
        //获得本机ip地址
        return inetAddress.getHostAddress();
    }
}
