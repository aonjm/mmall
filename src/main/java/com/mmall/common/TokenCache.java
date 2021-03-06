package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TokenCache {
    private static Logger logger=LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PROFIX="token_";
    private static LoadingCache<String,String> localCache=CacheBuilder.

            newBuilder().
            initialCapacity(1000).
            maximumSize(1000).
            expireAfterAccess(12,TimeUnit.HOURS).build(new CacheLoader<String, String>() {
        /**
         * 默认的数据加载数显，当调用get去找的时候，如果key没有对象的值，就调用这个方法加载
         * @param
         * @return
         * @throws Exception
         */
                @Override
        public String load(String s) throws Exception {
            return "null";
        }
    });

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        String value=null;
        try {
            value=localCache.get(key);
            if ("null".equals(value)){
                return null;
            }
        }catch (Exception e){
            logger.error("localCache get error",e);
        }
        return null;
    }
}
