/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Endpoints;

import Entities.UserEntity;
import Entities.CurrentStatusEntity;
import Entities.DriverEntity;
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
        long did = (long) Persistence.readUser(UserEntity.class, token).get("id");
        JSONObject query1 = new JSONObject();
        query1.put("driverId",did);
        JSONObject read = Persistence.readByProperties(CurrentStatusEntity.class, query1);
        
        if(read!=null){
            throw new AccessError(ERROR_TYPE.USER_NOT_ALLOWED);
        }

        JSONObject obj = new JSONObject();
        obj.put("vehicleId", resource);
        JSONObject readByProperties = Persistence.readByProperties(CurrentStatusEntity.class, obj);
        if (readByProperties.get("status") == null || (int) readByProperties.get("status") != 1) {
            throw new AccessError(ERROR_TYPE.ENTITY_UNAVAILABLE);

        } else {

            JSONObject query = new JSONObject();
            query.put("checkOutDate", new Date().toString());
            query.put("status", 2);
            
            JSONObject query2 = new JSONObject();
            query2.put("userId",did);
            query.put("driverId", Persistence.readByProperties(DriverEntity.class, query2).get("id"));
            
            return Persistence.update(CurrentStatusEntity.class, (int) readByProperties.get("id"), query);
        }
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 3);
        return Persistence.read(CurrentStatusEntity.class, resource);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        throw new AccessError(AccessError.ERROR_TYPE.OPERATION_FAILED);
    }

}
