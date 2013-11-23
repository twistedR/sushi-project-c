package model;

public class ApplicationModel {
	private final DAO dao;

	public ApplicationModel() throws ClassNotFoundException {
		dao = new DAO();
	}
}
