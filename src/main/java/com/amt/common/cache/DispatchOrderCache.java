/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cache;

import com.tna.common.ObjectCache;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class DispatchOrderCache {

    private static DispatchOrderCache realTimeCache;
    private static ObjectCache<Long, JSONObject> cache;
    private static ReentrantLock lock;

    private DispatchOrderCache() {
        cache = new ObjectCache();
        lock = new ReentrantLock();
    }

    public static DispatchOrderCache getInstance() {
        if (realTimeCache == null) {
            synchronized (DispatchOrderCache.class) {
                if (realTimeCache == null) {
                    realTimeCache = new DispatchOrderCache();

                }
            }
        }
        return realTimeCache;

    }

    public static void cache(Long key, JSONObject value) {
        lock.lock();
        try {
            DispatchOrderCache.getInstance().cache.cache(key, value);
        } finally {
            lock.unlock();
        }
    }

    public static JSONObject retreive(Long key) {
        JSONObject retreive;
        retreive = DispatchOrderCache.getInstance().cache.retreive(key);
        return retreive;
    }

    public static void clear(Long key) {
        lock.lock();
        try {
            DispatchOrderCache.getInstance().cache.remove(key);
        } finally {
            lock.unlock();
        }
    }

    public static Collection<JSONObject> getValues() {
        lock.lock();
        try {
            return DispatchOrderCache.getInstance().cache.getValues();
        } finally {
            lock.unlock();
        }
    }

    public static void setTimeStamp(Timestamp time) {
        lock.lock();
        try {
            DispatchOrderCache.getInstance().cache.setTimeStamp(time);
        } finally {
            lock.unlock();
        }
    }
    

    public static Timestamp getTimeStamp() {
        Timestamp timeStamp;
        timeStamp = DispatchOrderCache.getInstance().cache.getTimeStamp();
        return timeStamp;
    }

}
