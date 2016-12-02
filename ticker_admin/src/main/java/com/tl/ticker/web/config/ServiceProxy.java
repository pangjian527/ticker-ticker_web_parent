package com.tl.ticker.web.config;

import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.net.HostAndPort;
import com.tl.rpc.sys.SysUserService;
import org.springframework.beans.factory.FactoryBean;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * Created by pangjian on 16-12-2.
 */
public class ServiceProxy<T> implements FactoryBean{

    private final ThriftClientManager clientManager = new ThriftClientManager();

    private Class<T> clazz;
    private ServiceConfig serviceConfig;

    public ServiceProxy(Class<T> clazz,ServiceConfig serviceConfig){
        this.clazz = clazz;
        this.serviceConfig = serviceConfig;
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

    @Override
    public Object getObject() throws Exception {

        HostAndPort hostAndPort =
                HostAndPort.fromParts(serviceConfig.host, serviceConfig.port);
        return clientManager.createClient(new FramedClientConnector(hostAndPort),
                clazz).get();
    }

    @Override
    public Class<?> getObjectType() {
        return this.clazz;
    }


    @Override
    public boolean isSingleton() {
        return false;
    }
}
