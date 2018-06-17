/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.sessions;

import java.util.concurrent.locks.ReentrantLock;
import javax.websocket.Session;

/**
 *
 * @author tareq
 */
public class DriverSession {

    private ReentrantLock lock;
    private String token;
    private long vehicleId;
    private boolean available;
    private Session userSession;

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long id) {
        this.vehicleId = id;
    }
    

    public void lock(){
        this.lock.lock();
    }

    public void unlock() {
        this.lock.unlock();
    }
    
    public boolean isLocked(){
        return this.lock.isLocked();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Session getUserSession() {
        return userSession;
    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }

    public DriverSession(String token, Session userSession, long vehicleId) {
        this.available = true;
        this.vehicleId = vehicleId;
        this.lock = new ReentrantLock();
        this.token = token;
        this.userSession = userSession;
    }
    
}
