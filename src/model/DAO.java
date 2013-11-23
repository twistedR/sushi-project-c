package model;

public class DAO {
	private final String DB_URL = "jdbc:derby://localhost:9999/CSE;user=student;password=secret;";

	public DAO() throws ClassNotFoundException {
		Class.forName("org.apache.derby.jdbc.ClientDriver");
	}
}
