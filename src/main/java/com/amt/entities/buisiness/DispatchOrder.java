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

    double startLatitude;
    double startLongitude;
    Date creationDate;
    Date startDate;
    Date completionDate;
    long vehicleId;
    String firstName;
    String lastName;
    String phoneNumber;
    String notes;
    long status;
    long destinationHospitalId;
    long dispatcherId;

}
