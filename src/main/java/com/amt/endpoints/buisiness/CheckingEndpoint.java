/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.common.sessions.DriverSessionManager;
import com.amt.entities.management.Driver;
import com.amt.entities.history.StatusHistory;
import com.amt.entities.auth.User;
import com.amt.entities.buisiness.Vehicle;
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
@WebServlet("/vehicle/checking/*")
public class CheckingEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        long level = (long) user.get("level");
        if (level == 1 || level == 3) {
            JSONObject query = new JSONObject();
            query.put("status", 0);
            return Persistence.listByProperties(Vehicle.class, query);
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
        }
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);

    }

    @Override
    public JSONObject doUpdate(JSONObject json, long resource, String token) throws AccessError {

        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if ((long) user.get("level") == 1) {

            JSONObject driverQuery = new JSONObject();
            driverQuery.put("userId", user.get("id"));
            JSONObject readDriver = Persistence.readByProperties(Driver.class, driverQuery);

            JSONObject vehicleQuery = new JSONObject();
            vehicleQuery.put("driver", readDriver.get("id"));
            JSONObject readVehicle = Persistence.readByProperties(Vehicle.class, vehicleQuery);
            if (readVehicle != null) {
                if ((int) readVehicle.get("status") == 1) {

                    JSONObject statusHistoryQuery = new JSONObject();
                    statusHistoryQuery.put("vehicleId", readVehicle.get("id"));
                    statusHistoryQuery.put("userId", user.get("id"));
                    statusHistoryQuery.put("fromValue", readVehicle.get("status"));
                    statusHistoryQuery.put("toValue", 0);
 
                    JSONObject query = new JSONObject();
                    query.put("driver", null);
                    query.put("status", 0);

                    JSONObject response = Persistence.update(Vehicle.class, (int)readVehicle.get("id"), query);
                    Persistence.create(StatusHistory.class, statusHistoryQuery);
                    return response;
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
            JSONObject readVehicle = Persistence.read(Vehicle.class, resource);
            if ((int) readVehicle.get("status") == 0) {
                JSONObject driverQuery = new JSONObject();
                driverQuery.put("userId", user.get("id"));
                JSONObject readDriver = Persistence.readByProperties(Driver.class, driverQuery);

                JSONObject statusHistoryQuery = new JSONObject();
                statusHistoryQuery.put("vehicleId", readVehicle.get("id"));
                statusHistoryQuery.put("userId", user.get("id"));
                statusHistoryQuery.put("fromValue", readVehicle.get("status"));
                statusHistoryQuery.put("toValue", 1);

                JSONObject vehicleQuery = new JSONObject();
                vehicleQuery.put("driver", readDriver.get("id"));
                vehicleQuery.put("status", 1);

                JSONObject response = Persistence.update(Vehicle.class, resource, vehicleQuery);
                Persistence.create(StatusHistory.class, statusHistoryQuery);
                return response;
            } else {
                throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
            }
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_AUTHORISED);
        }

    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

}
