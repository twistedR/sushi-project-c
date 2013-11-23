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

	public List<ItemBean> getItems(int catId, int offset, int count)
			throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql;
			PreparedStatement ps;
			if (catId == -1) {
				// WE need all items
				sql = "SELECT i.*, c.NAME as cat_name, c.ID as CAT_ID, c.DESCRIPTION as CAT_DES,"
						+ " c.PICTURE as CAT_PIC from ITEM i, CATEGORY c WHERE c.ID=i.CATID"
						+ " offset "
						+ offset
						+ " rows fetch first "
						+ count
						+ " rows only";
				ps = conn.prepareStatement(sql);
			} else {
				sql = "SELECT i.*, c.NAME as cat_name, c.ID as CAT_ID, c.DESCRIPTION as CAT_DES,"
						+ " c.PICTURE as CAT_PIC from ITEM i, CATEGORY c WHERE i.CATID = ? AND c.ID=i.CATID"
						+ " offset "
						+ offset
						+ " rows fetch first "
						+ count
						+ " rows only";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, catId);
			}

			ResultSet rs = ps.executeQuery();
			List<ItemBean> items = new ArrayList<ItemBean>();
			while (rs.next()) {
				CategoryBean c = new CategoryBean();
				ItemBean i = new ItemBean();

				// Category STuff
				c.setId(rs.getInt("CAT_ID"));
				c.setName(rs.getString("CAT_NAME"));
				c.setDescription(rs.getString("CAT_DES"));
				c.setPicture(rs.getBlob("CAT_PIC"));

				// Item
				i.setNumber(rs.getString("NUMBER"));
				i.setName(rs.getString("NAME"));
				i.setPrice(rs.getDouble("PRICE"));
				i.setQuantity(rs.getInt("QTY"));
				i.setOnorder(rs.getInt("ONORDER"));
				i.setReorder(rs.getInt("REORDER"));
				i.setCatid(rs.getInt("CATID"));
				i.setSupid(rs.getInt("SUPID"));
				i.setCostprice(rs.getDouble("COSTPRICE"));
				i.setUnit(rs.getString("UNIT"));

				// Attach category
				i.setCategory(c);

				items.add(i);

			}
			return items;
		}

	}

	public int getItemsCount(int catId) throws SQLException{
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql;
			PreparedStatement ps;
			if (catId == -1) {
				// WE need all items
				sql = "SELECT COUNT(*) FROM ITEM";
				ps = conn.prepareStatement(sql);
			} else {
				sql = "SELECT COUNT(*) FROM ITEM WHERE CATID=?";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, catId);
			}
			
			ResultSet rs = ps.executeQuery();
			int count = 0;
			if(rs.next()){
				count = rs.getInt(1);
			}
			return count;
		}
	}

	public List<CategoryBean> getCategories() throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql = "SELECT * FROM CATEGORY";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<CategoryBean> c = new ArrayList<CategoryBean>();
			while (rs.next()) {
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
