package com.tl.ticker.web.config;

import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.net.HostAndPort;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by pangjian on 16-12-5.
 */
public class ProxyHandler implements InvocationHandler,Cloneable {

    private ServiceConfig config;
    private Class clazz;

    public ProxyHandler(ServiceConfig config,Class clazz){
        this.config = config;
        this.clazz = clazz;
    }

    private final ThriftClientManager clientManager = new ThriftClientManager();

    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        HostAndPort hostAndPort =
                HostAndPort.fromParts(this.config.host, this.config.port);

        Object target = clientManager.createClient(new FramedClientConnector(hostAndPort),
                this.clazz).get();

        return method.invoke(target,objects);
    }

    public void close() throws IOException {
        closeQuietly(this.clientManager);
    }

    protected static void closeQuietly(Closeable closeable) {
        if(null != closeable) {
            try {
                closeable.close();
            } catch (IOException var2) {
                ;
            }
        }
    }
}
