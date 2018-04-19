/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Endpoints;

import Entities.UserEntity;
import Entities.CurrentStatusEntity;
import Entities.HistoricalStatusEntity;
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
@WebServlet("/checkout/*")
public class CheckOutEndpoint extends AuthorisedEndpoint {

    @Override
    public JSONObject doList(String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 1);
        JSONObject obj = new JSONObject();
        obj.put("vehicleId", resource);
        JSONObject readByProperties = Persistence.readByProperties(CurrentStatusEntity.class, obj);
        if (readByProperties.get("status") == null || (int) readByProperties.get("status") != 1) {
            throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);

        } else {
            Persistence.create(HistoricalStatusEntity.class, readByProperties);
            obj.remove("vehicleId");
            obj.put("checkInDate", "");
            obj.put("checkOutDate", new Date().toString());
            obj.put("status", 2);
            long did = (long) Persistence.readUser(UserEntity.class, token).get("id");
            obj.put("driverId", did);
            return Persistence.update(CurrentStatusEntity.class, (int) readByProperties.get("id"), obj);
        }
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 2);
        return Persistence.read(CurrentStatusEntity.class, resource);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

}
