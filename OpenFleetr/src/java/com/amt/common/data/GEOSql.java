/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amt.common.data;

import com.tna.common.AccessError;
import com.tna.common.AccessError.ERROR_TYPE;
import com.tna.data.Access;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author tareq
 */
public class GEOSql {

    private static final String WRITE_POLYGON_SQL = "INSERT INTO %s (name,polygon) VALUES (?,ST_GEOMFROMTEXT('Polygon((? ?, ? ?, ? ?, ? ?,? ?))'));";
    private static final String READ_POLYGON_SQL = "SELECT ST_AsText(polygon) FROM %s where id=?";
    private static final String LIES_WITHIN_SQL = "SELECT id FROM %s WHERE ST_CONTAINS(polygon,ST_GEOMFROMTEXT('POINT(? ?)'))";

    public static String readPolygon(Class geoEntity, long l) throws AccessError {
        String result = null;
        String geoEntityClassName = geoEntity.getSimpleName();
        Connection conn = Access.pool.checkOut();
        try (PreparedStatement pstmt = conn.prepareStatement(String.format(READ_POLYGON_SQL, geoEntityClassName))) {
            pstmt.setObject(1, l);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = rs.getString(1);
                } else {
                    throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
                }

            }
        } catch (SQLException ex) {
        } finally {
            Access.pool.checkIn(conn);
        }
        return result;

    }

    public static JSONObject writePolygon(Class geoEntity, JSONObject coords) throws AccessError {
        JSONObject result = new JSONObject();
        String geoEntityClassName = geoEntity.getSimpleName();
        Connection conn = Access.pool.checkOut();
        try (PreparedStatement pstmt = conn.prepareStatement(String.format(WRITE_POLYGON_SQL, geoEntityClassName),Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setObject(1, coords.get("name"));
            pstmt.setObject(2, coords.get("latitude1"));
            pstmt.setObject(3, coords.get("longitude1"));
            pstmt.setObject(4, coords.get("latitude2"));
            pstmt.setObject(5, coords.get("longitude2"));
            pstmt.setObject(6, coords.get("latitude3"));
            pstmt.setObject(7, coords.get("longitude3"));
            pstmt.setObject(8, coords.get("latitude4"));
            pstmt.setObject(9, coords.get("longitude4"));
            pstmt.setObject(10,coords.get("latitude1"));
            pstmt.setObject(11,coords.get("longitude1"));
            pstmt.execute();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new AccessError(ERROR_TYPE.OPERATION_FAILED);
                }
                result.put("key", rs.getLong(1));
            }

        } catch (SQLException ex) {
            result = null;
        } finally {
            Access.pool.checkIn(conn);

        }
        return result;
    }

    public static long liesWithinPolygon(Class geoEntity, JSONObject coords) throws AccessError {
        String geoEntityClassName = geoEntity.getSimpleName();
        Connection conn = Access.pool.checkOut();
        long result = -1;
        try (PreparedStatement pstmt = conn.prepareStatement(String.format(LIES_WITHIN_SQL, geoEntityClassName))) {
            pstmt.setObject(1, coords.get("latitude"));
            pstmt.setObject(2, coords.get("longitudee"));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = rs.getLong(1);
                } else {
                    throw new AccessError(ERROR_TYPE.ENTITY_NOT_FOUND);
                }

            }
        } catch (SQLException ex) {

        } finally {
            Access.pool.checkIn(conn);
        }
        return result;
    }
    
    

}
