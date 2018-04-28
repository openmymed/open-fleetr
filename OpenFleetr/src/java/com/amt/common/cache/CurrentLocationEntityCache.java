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
public class CurrentLocationEntityCache {
    public static CurrentLocationEntityCache locationCache = CurrentLocationEntityCache.getInstance();
    public static ObjectCache<Long,JSONObject> cache;
    
    public static CurrentLocationEntityCache getInstance(){
        if(locationCache == null){
            return new CurrentLocationEntityCache();
        }else{
            return locationCache;
        }
    }
    
    private CurrentLocationEntityCache(){
        cache = new ObjectCache();
        cache.setTimeStamp(new Timestamp(0));
    }
}
