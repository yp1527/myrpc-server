package com.yp.myrpc.server;

import com.yp.myrpc.server.domain.RpcRequest;
import org.springframework.util.StringUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * 处理客户端请求
 */
public class ProcessorHandler implements Runnable {

    private Socket socket;
    private Map<String,Object> serverData;

    public ProcessorHandler(Socket socket, Map<String,Object> serverData) {
        this.socket = socket;
        this.serverData = serverData;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream=null;
        ObjectOutputStream objectOutputStream=null;
        try {
            objectInputStream=new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest= (RpcRequest) objectInputStream.readObject();
            Object result=process(rpcRequest);
            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(objectInputStream!=null){
                    objectInputStream.close();
                }
                if(objectOutputStream!=null){
                    objectOutputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据RpcRequest信息解析出要调用的服务类 并进行反射调用
     * @param request
     * @return
     */
    private Object process(RpcRequest request){
        try {
            String serviceName=request.getClassName();
            String version=request.getVersion();
            //增加版本号的判断
            if(!StringUtils.isEmpty(version)){
                serviceName+="-"+version;
            }
            Object service=serverData.get(serviceName);
            if(service==null){
                throw new RuntimeException("service not found:"+serviceName);
            }

            Object[] args=request.getArgs(); //拿到客户端请求的参数
            Method method=null;
            //加载请求调用的类
            Class clazz=Class.forName(request.getClassName());
            if(args!=null) {
                Class<?>[] types = new Class[args.length]; //获得每个参数的类型
                for (int i = 0; i < args.length; i++) {
                    types[i] = args[i].getClass();
                }
                //找到类中匹配的方法
                method=clazz.getMethod(request.getMethodName(),types);
            }else{
                method=clazz.getMethod(request.getMethodName());
            }
            //反射调用
            return method.invoke(service,args);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
