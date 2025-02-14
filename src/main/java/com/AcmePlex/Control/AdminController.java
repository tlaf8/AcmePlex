package com.AcmePlex.Control;

import com.AcmePlex.Database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminController {
    public boolean validateAdminKey(String adminKey) {
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            String query = "SELECT * FROM ADMIN WHERE adminKey = ?";
            PreparedStatement stmt = db_conn.getConnection().prepareStatement(query);
            stmt.setString(1, adminKey);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            boolean isValid = rs.getString("adminKey").equals(adminKey);
            rs.close();
            stmt.close();
            return isValid;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
