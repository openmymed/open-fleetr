/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cache;

import com.tna.common.ObjectCache;
import java.sql.Timestamp;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class CurrentDispatchOrderEntityCache {
    
    private static CurrentDispatchOrderEntityCache realTimeCache ;
    private static ObjectCache<Long,JSONObject> cache;
    
    private CurrentDispatchOrderEntityCache(){
        cache = new ObjectCache();
    }
    
    public static synchronized CurrentDispatchOrderEntityCache getInstance(){
        if(realTimeCache==null){
             realTimeCache = new CurrentDispatchOrderEntityCache();
        }
            return realTimeCache;

    }
    
    public static void cache(Long key, JSONObject value){
        CurrentDispatchOrderEntityCache.getInstance().cache.cache(key, value);
    }
    
    public static JSONObject retreive(Long key){
       return CurrentDispatchOrderEntityCache.getInstance().cache.retreive(key);
    }
    
    public static void setTimeStamp(Timestamp time){
        CurrentDispatchOrderEntityCache.getInstance().cache.setTimeStamp(time);
    }
    
    public static Timestamp getTimeStamp(){
        return CurrentDispatchOrderEntityCache.getInstance().cache.getTimeStamp();
    }
   
}
