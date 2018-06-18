/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.endpoints.buisiness;

import com.amt.entities.auth.User;
import com.amt.entities.buisiness.Vehicle;
import com.amt.entities.history.LocationHistory;
import com.amt.entities.management.Driver;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
@WebServlet("/vehicle/*")
public class VehicleEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 2);
        return Persistence.list(Vehicle.class);
    }

    @Override
    public JSONObject doCreate(JSONObject data, String token) throws AccessError {
        JSONObject user = UserAccessControl.fetchUserByToken(User.class, token);
        if ((long)user.get("level") == 1) {

            JSONObject driverQuery = new JSONObject();
            driverQuery.put("userId", user.get("id"));
            JSONObject driver = Persistence.readByProperties(Driver.class, driverQuery);

            JSONObject vehicleQuery = new JSONObject();
            vehicleQuery.put("driver", driver.get("id"));
            JSONObject vehicle = Persistence.readByProperties(Vehicle.class, vehicleQuery);

            if (vehicle == null) {
                throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);
            }

            long vehicleId = (int) vehicle.get("id");

            JSONObject query = new JSONObject();
            query.put("latitude", data.get("latitude"));
            query.put("longitude", data.get("longitude"));
            JSONObject response = Persistence.update(Vehicle.class, vehicleId, query);
            
            query.put("vehicleId", vehicle.get("id"));
            Persistence.create(LocationHistory.class, query);

            return response;
        } else {
            throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
        }
    }

    @Override
    public JSONObject doUpdate(JSONObject data, long resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doRead(long resource, String token) throws AccessError {
        UserAccessControl.authOperation(User.class, token, 2);
        return Persistence.read(Vehicle.class, resource);
    }

    @Override
    public JSONObject doDelete(long resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

}
