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

	public List<ItemBean> getItems(int catId, int offset, int count,
			double minPrice, double maxPrice, String sort) throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql;
			PreparedStatement ps;
			int sp = 1;
			if (catId == -1) {
				// WE need all items
				sql = "SELECT i.*, c.NAME as cat_name, c.ID as CAT_ID, c.DESCRIPTION as CAT_DES,"
						+ " c.PICTURE as CAT_PIC from ITEM i, CATEGORY c WHERE c.ID=i.CATID "
						+ "AND i.PRICE > ? AND i.PRICE < ? "
						+ "ORDER BY i."
						+ sort
						+ " offset "
						+ offset
						+ " rows fetch first "
						+ count + " rows only";
				ps = conn.prepareStatement(sql);
			} else {
				sql = "SELECT i.*, c.NAME as cat_name, c.ID as CAT_ID, c.DESCRIPTION as CAT_DES,"
						+ " c.PICTURE as CAT_PIC from ITEM i, CATEGORY c WHERE i.CATID = ? AND c.ID=i.CATID "
						+ "AND i.PRICE > ? AND i.PRICE < ? "
						+ "ORDER BY i."
						+ sort
						+ " offset "
						+ offset
						+ " rows fetch first "
						+ count + " rows only";
				ps = conn.prepareStatement(sql);
				ps.setInt(sp, catId);
				sp++;
			}

			ps.setDouble(sp, minPrice);

			sp++;
			ps.setDouble(sp, maxPrice);

			ResultSet rs = ps.executeQuery();
			List<ItemBean> items = new ArrayList<ItemBean>();
			while (rs.next()) {
				items.add(rs2item(rs));

			}
			return items;
		}

	}

	public int getItemsCount(int catId, double minPrice, double maxPrice) throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql;
			PreparedStatement ps;
			int sp =1;
			if (catId == -1) {
				// WE need all items
				sql = "SELECT count(*) from ITEM WHERE "
						+ "PRICE > ? AND PRICE < ?";
				ps = conn.prepareStatement(sql);
			} else {
				sql = "SELECT COUNT(*) FROM ITEM WHERE CATID=? AND "
						+ "PRICE > ? AND PRICE < ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(sp, catId);
				sp++;
			}
			ps.setDouble(sp, minPrice);
			sp++;
			ps.setDouble(sp, maxPrice);

			ResultSet rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return count;
		}
	}
	
	public int getSearchItemsCount(String search_term,int catId, double minPrice, double maxPrice) throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql;
			PreparedStatement ps;
			int sp =1;
			if (catId == -1) {
				// WE need all items
				sql = "SELECT count(*) from ITEM WHERE "
						+ "(lower(i.NAME) like ? OR lower(i.NUMBER) like ?) AND "
						+ "PRICE > ? AND PRICE < ?";
				ps = conn.prepareStatement(sql);
			} else {
				sql = "SELECT COUNT(*) FROM ITEM WHERE CATID=? AND "
						+ "(lower(i.NAME) like ? OR lower(i.NUMBER) like ?) AND "
						+ "PRICE > ? AND PRICE < ?";
				ps = conn.prepareStatement(sql);
				ps.setInt(sp, catId);
				sp++;
			}
			ps.setDouble(sp, minPrice);
			sp++;
			ps.setDouble(sp, maxPrice);

			ResultSet rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
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

	public List<ItemBean> getSearchItems(String search_term, int catId,
			int offset, int count, double minPrice, double maxPrice, String sort)
			throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql;
			PreparedStatement ps;
			search_term = search_term.toLowerCase();
			int sp = 1;
			if (catId == -1) {
				// WE need all items
				sql = "SELECT i.*, c.NAME as cat_name, c.ID as CAT_ID, c.DESCRIPTION as CAT_DES,"
						+ " c.PICTURE as CAT_PIC from ITEM i, CATEGORY c WHERE c.ID=i.CATID "
						+ "AND i.PRICE > ? AND i.PRICE < ? AND "
						+ "(lower(i.NAME) like ? OR lower(i.NUMBER) like ?) "
						+ "ORDER BY i."
						+ sort
						+ " offset "
						+ offset
						+ " rows fetch first " + count + " rows only";
				ps = conn.prepareStatement(sql);
			} else {
				sql = "SELECT i.*, c.NAME as cat_name, c.ID as CAT_ID, c.DESCRIPTION as CAT_DES,"
						+ " c.PICTURE as CAT_PIC from ITEM i, CATEGORY c WHERE i.CATID = ? AND c.ID=i.CATID "
						+ "AND i.PRICE > ? AND i.PRICE < ? AND "
						+ "(lower(i.NAME) LIKE ? OR lower(i.NUMBER) LIKE ?) "
						+ "ORDER BY i."
						+ sort
						+ " offset "
						+ offset
						+ " rows fetch first " + count + " rows only";
				ps = conn.prepareStatement(sql);
				ps.setInt(sp, catId);
				sp++;
			}

			ps.setDouble(sp, minPrice);
			sp++;
			ps.setDouble(sp, maxPrice);
			sp++;
			ps.setString(sp, "%" + search_term + "%");
			sp++;
			ps.setString(sp, "%" + search_term + "%");
			ResultSet rs = ps.executeQuery();
			List<ItemBean> items = new ArrayList<ItemBean>();
			while (rs.next()) {

				items.add(rs2item(rs));

			}
			return items;
		}
	}

	public ItemBean getItem(String itemNumber) throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql;
			PreparedStatement ps;
			sql = "SELECT i.*, c.NAME as cat_name, c.ID as CAT_ID, c.DESCRIPTION as CAT_DES,"
					+ " c.PICTURE as CAT_PIC from ITEM i, CATEGORY c WHERE c.ID=i.CATID AND i.NUMBER = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, itemNumber);
			ItemBean i = null;
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				i = rs2item(rs);
			}
			return i;
		}
	}

	private ItemBean rs2item(ResultSet rs) throws SQLException {
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

		return i;
	}

	public ClientBean getClient(int clientNumber, String password)
			throws SQLException {
		try (Connection conn = DriverManager.getConnection(DB_URL);
				Statement statement = conn.createStatement()) {
			statement.executeUpdate("SET SCHEMA roumani");
			String sql;
			PreparedStatement ps;
			sql = "SELECT * from CLIENT where number=? and password=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, clientNumber);
			ps.setString(2, password.trim());
			ClientBean client = null;
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				client = new ClientBean();
				client.setName(rs.getString("NAME"));
				client.setNumber(rs.getInt("NUMBER"));
				client.setPassword(rs.getString("PASSWORD"));
				client.setRating(rs.getString("RATING"));

			}
			return client;
		}
	}
}
