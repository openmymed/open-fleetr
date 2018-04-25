/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.entities;

import com.tna.entities.BasicEntity;

/**
 *
 * @author tareq
 */
public class CurrentStatusEntity extends BasicEntity{
    
    public long vehicleId;
    public long driverId;
    public String checkOutDate;
    public String checkInDate;
    public long status;
    public String notes;
    
}
