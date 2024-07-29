package com.example.demo.loaddata;

import com.example.demo.datacon.DataBaseConnection;
import com.example.demo.entity.Observer.Brand;
import com.example.demo.entity.Observer.Observer;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {
    private static BrandDAO instance;
    private BrandDAO(){
    }
    public static BrandDAO getInstance() throws Exception {
        if (instance == null) {
            instance = new BrandDAO();
        }
        return instance;
    }


    Connection conn = null;
    PreparedStatement ps, ps1, ps2, ps3 = null;
    ResultSet rs = null;

    public List<User> getUsersByBrand (String brandName) {
        List<User> list = new ArrayList<>();
        String query = "Select Name, user.Email from user_brand join user on user_brand.Email = user.Email where brandname=?";
        try {
            conn = DataBaseConnection.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, brandName);
            rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();

                user.setName(rs.getString(1));
                user.setEmail(rs.getString(2));

                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void userFollowBrand (String email, String brandName) {
        String query = "insert into user_brand(email, brandname) values (?, ?)";
        try {
            conn = DataBaseConnection.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, brandName);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void userUnfollowBrand (String email, String brandName) {
        String query = "delete from user_brand where email= ? AND brandName = ?";
        try {
            conn = DataBaseConnection.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, brandName);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Brand> getUserBrands (String email) {
        List<Brand> list = new ArrayList<>();
        String query = "Select brandName From user_brand where email=?";
        try {
            conn = DataBaseConnection.getInstance().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                Brand brand = new Brand();
                brand.setBrandName(rs.getString(1));

                list.add(brand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
