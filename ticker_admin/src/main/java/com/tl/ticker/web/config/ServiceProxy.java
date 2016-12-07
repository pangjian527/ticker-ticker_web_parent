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
    @Override
    public Object getObject() throws Exception {

        ProxyHandler handler = new ProxyHandler(serviceConfig,clazz);

       return clazz.cast(Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz,Closeable.class},handler));
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
