/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.entities.history;

import com.tna.entities.BasicEntity;
import java.sql.Date;

/**
 *
 * @author tareq
 */
public class HistoricalStatusEntity extends BasicEntity {
    
    public long vehicleId;
    public long driverId;
    public Date checkOutDate;
    public Date checkInDate;
    public long status;
    public String notes;
    
}
