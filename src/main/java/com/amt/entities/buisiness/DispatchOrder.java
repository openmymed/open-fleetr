/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.entities.buisiness;

import com.tna.entities.BasicEntity;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author tareq
 */
public class DispatchOrder extends BasicEntity {

    public double startLatitude;
    public double startLongitude;
    public Date creationDate;
    public Date startDate;
    public Date completionDate;
    public long vehicleId;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String notes;
    public long status;
    public long destinationHospitalId;
    public long dispatcherId;

}
