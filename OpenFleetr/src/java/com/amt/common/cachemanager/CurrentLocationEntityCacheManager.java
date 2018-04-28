/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.cachemanager;

import com.amt.common.cache.CurrentLocationEntityCache;
import com.amt.entities.CurrentLocationEntity;
import com.tna.common.AccessError;
import com.tna.data.Persistence;
import java.sql.Timestamp;
import java.util.Set;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class CurrentLocationEntityCacheManager implements Runnable {

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            try {
                JSONObject differentialList = Persistence.listNewerThan(CurrentLocationEntity.class, CurrentLocationEntityCache.cache.getTimeStamp());
                if (differentialList != null) {
                    Set keySet = differentialList.keySet();
                    for (Object key : keySet) {
                        JSONObject listItem = (JSONObject) differentialList.get(key);
                        CurrentLocationEntityCache.cache.cache((long)listItem.get("vehicleId"), listItem);
                    }
                }

            } catch (AccessError ex) {
                handleError(ex);
            } finally {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {

                }
                CurrentLocationEntityCache.cache.setTimeStamp(now);
            }

        }
    }

    public static void handleError(AccessError ex) {

    }

    public CurrentLocationEntityCacheManager() {
        CurrentLocationEntityCache.getInstance();
    }
}
