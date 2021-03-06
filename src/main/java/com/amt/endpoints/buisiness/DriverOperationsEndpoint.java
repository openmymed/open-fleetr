/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.common.sessions.DriverSessionManager;
import com.amt.entities.auth.User;
import com.amt.entities.buisiness.DispatchOrder;
import com.amt.entities.buisiness.Vehicle;
import com.amt.entities.management.Driver;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/user/driver/dispatch/*")
public class DriverOperationsEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if ((long) user.get("level") == 1) {

            JSONObject driverQuery = new JSONObject();
            driverQuery.put("userId", user.get("id"));
            JSONObject driver = Persistence.readByProperties(Driver.class, driverQuery);

            JSONObject vehicleQuery = new JSONObject();
            vehicleQuery.put("driver", driver.get("id"));
            JSONObject vehicle = Persistence.readByProperties(Vehicle.class, vehicleQuery);

            JSONObject orderQuery = new JSONObject();
            orderQuery.put("vehicleId", vehicle.get("id"));
            orderQuery.put("status", 0);

            return Persistence.listByProperties(DispatchOrder.class, orderQuery);

        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }
    }

    @Override
    public JSONObject doCreate(JSONObject data, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject data, long resource, String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if ((long) user.get("level") == 1) {

            JSONObject driverQuery = new JSONObject();
            driverQuery.put("userId", user.get("id"));
            JSONObject driver = Persistence.readByProperties(Driver.class, driverQuery);

            JSONObject readOrder = Persistence.read(DispatchOrder.class, resource);

            JSONObject vehicleQuery = new JSONObject();
            vehicleQuery.put("driver", driver.get("id"));
            JSONObject vehicle = Persistence.readByProperties(Vehicle.class, vehicleQuery);

            if ((int) readOrder.get("vehicleId") == (int) vehicle.get("id")) {

                if ((int) vehicle.get("status") == 2 && (int) readOrder.get("status") == 1) {

                    JSONObject completion = new JSONObject();
                    completion.put("completionDate", new Date().toString());
                    completion.put("status", 2);
                    
                    vehicleQuery = new JSONObject();
                    vehicleQuery.put("status", 1);
                    Persistence.update(Vehicle.class, (int) vehicle.get("id"), vehicleQuery);

                    DriverSessionManager.getDriverSession((long)(int)vehicle.get("id")).pop();
                    DriverSessionManager.setAvailable((int) vehicle.get("id"), true);
                    return Persistence.update(DispatchOrder.class, resource, completion);

                } else {
                    throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
                }
            } else {
                throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
            }

        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if ((long) user.get("level") == 1) {

            JSONObject driverQuery = new JSONObject();
            driverQuery.put("userId", user.get("id"));
            JSONObject driver = Persistence.readByProperties(Driver.class, driverQuery);
            
            JSONObject readOrder = Persistence.read(DispatchOrder.class, resource);
            
            JSONObject vehicleQuery = new JSONObject();
            vehicleQuery.put("driver", driver.get("id"));
            JSONObject vehicle = Persistence.readByProperties(Vehicle.class, vehicleQuery);

            if ((int) readOrder.get("vehicleId") == (int) vehicle.get("id")) {

                if ((int) vehicle.get("status") == 1 && (int) readOrder.get("status") == 0) {

                    JSONObject starting = new JSONObject();
                    starting.put("startDate", new Date().toString());
                    starting.put("status", 1);

                    vehicleQuery = new JSONObject();
                    vehicleQuery.put("status",2);
                    Persistence.update(Vehicle.class, (long)(int) vehicle.get("id"), vehicleQuery);

                    DriverSessionManager.setAvailable((int) vehicle.get("id"), false);
                    return Persistence.update(DispatchOrder.class, resource, starting);

                } else {
                    throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
                }
            } else {
                throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
            }

        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

}
