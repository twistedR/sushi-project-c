package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAO {
	private final String DB_URL = "jdbc:derby://localhost:9999/CSE;user=student;password=secret;";

	public DAO() throws ClassNotFoundException {
		Class.forName("org.apache.derby.jdbc.ClientDriver");
	}

	public List<ItemBean> getItems(int catId) throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql;
			if(catId == -1){
				//WE need all items
				sql = "SELECT i.*, c.NAME as cat_name, c.ID as CAT_ID";
			}
		} 
		return null;
	}

	public List<CategoryBean> getCategories() throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql = "SELECT * FROM CATEGORY";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			List<CategoryBean> c = new ArrayList<CategoryBean>();
			while(rs.next()){
				CategoryBean cat = new CategoryBean();
				cat.setId(rs.getInt("ID"));
				cat.setName(rs.getString("NAME"));
				cat.setDescription(rs.getString("DESCRIPTION"));
				cat.setPicture(rs.getBlob("PICTURE"));
				c.add(cat);
			}
			return c;
		} 
	}
}
