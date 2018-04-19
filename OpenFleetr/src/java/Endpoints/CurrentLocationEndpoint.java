package Endpoints;

import Entities.CurrentLocationEntity;
import Entities.DriverEntity;
import Entities.HistoricalLocationEntity;
import Entities.UserEntity;
import Entities.CurrentStatusEntity;
import Entities.HistoricalStatusEntity;
import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.common.UserAccessControl;
import com.tna.data.Persistence;
import com.tna.endpoints.AuthorisedEndpoint;
import javax.servlet.annotation.WebServlet;
import org.json.simple.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tareq
 */

@WebServlet("/location/*")
public class CurrentLocationEndpoint extends AuthorisedEndpoint{

     @Override
    public JSONObject doList(String token) throws AccessError {
       UserAccessControl.authOperation(UserEntity.class, token, 2);
       return Persistence.list(CurrentLocationEntity.class);
    }

    @Override
    public JSONObject doCreate(JSONObject json, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }

    @Override
    public JSONObject doUpdate(JSONObject json, int resource, String token) throws AccessError {
        UserAccessControl.authOperation(UserEntity.class, token, 1);
        JSONObject obj = new JSONObject();
        obj.put("vehicleId",resource);
        JSONObject obj2 = Persistence.readByProperties(CurrentLocationEntity.class,obj);
        Persistence.create(HistoricalLocationEntity.class, obj);
        return  Persistence.update(CurrentLocationEntity.class,(long)obj2.get("id"),json);
    }

    @Override
    public JSONObject doRead(int resource, String token) throws AccessError {
        JSONObject obj = new JSONObject();
        obj.put("vehicleId",resource);
        return Persistence.readByProperties(CurrentLocationEntity.class,obj);
    }

    @Override
    public JSONObject doDelete(int resource, String token) throws AccessError {
        throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
    }
    
}
