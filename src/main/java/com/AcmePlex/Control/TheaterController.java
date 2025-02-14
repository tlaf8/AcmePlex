package com.AcmePlex.Control;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.AcmePlex.Database.DatabaseConnection;
import com.AcmePlex.Entity.Theater;

public class TheaterController {
    public List<Theater> getAllTheaters() {
        List<Theater> theaters = new ArrayList<>();
        try {
            DatabaseConnection db_conn = new DatabaseConnection();
            String query = "SELECT TheaterID, TName, Location FROM THEATERS";
            PreparedStatement stmt = db_conn.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                theaters.add(new Theater(rs.getInt("TheaterID"), rs.getString("Location")));
            }

            rs.close();
            stmt.close();
            db_conn.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return theaters;
    }
}
