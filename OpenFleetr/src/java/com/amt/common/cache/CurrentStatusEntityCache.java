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
public class CurrentStatusEntityCache {
    public static CurrentStatusEntityCache statusCache = CurrentStatusEntityCache.getInstance();
    public static ObjectCache<Long,JSONObject> cache;
    
    public static CurrentStatusEntityCache getInstance(){
        if(statusCache == null){
            return new CurrentStatusEntityCache();
        }else{
            return statusCache;
        }
    }
    
    private CurrentStatusEntityCache(){
        cache = new ObjectCache();
        cache.setTimeStamp(new Timestamp(0));
    }
}
