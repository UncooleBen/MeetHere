package com.webapp.model.user;

public class User {
	private int _id;
	private String _username;
	private String _password;
	private String _name;
	private Gender _sex;
	private int _permission;
	private String _tel;

	public User() {

	}

	public User(String username, String password, String name, String sex, String tel) {
		super();
		this._id = 0;
		this._username = username;
		this._password = password;
		this._name = name;
		this._sex = Gender.valueOf(sex);
		this._permission = 1;
		this._tel = tel;
	}

	public User(int id, String username, String password, String name, String sex, String tel) {
		super();
		this._id = id;
		this._username = username;
		this._password = password;
		this._name = name;
		this._sex = Gender.valueOf(sex);
		this._permission = 1;
		this._tel = tel;
	}

	/**
	 * @return the _username
	 */
	public String get_username() {
		return _username;
	}

	/**
	 * @param _username the _username to set
	 */
	public void set_username(String _username) {
		this._username = _username;
	}

	/**
	 * @return the _password
	 */
	public String get_password() {
		return _password;
	}

	/**
	 * @param _password the _password to set
	 */
	public void set_password(String _password) {
		this._password = _password;
	}

	/**
	 * @return the _name
	 */
	public String get_name() {
		return _name;
	}

	/**
	 * @param _name the _name to set
	 */
	public void set_name(String _name) {
		this._name = _name;
	}

	/**
	 * @return the _sex
	 */
	public Gender get_sex() {
		return _sex;
	}

	/**
	 * @param _sex the _sex to set
	 */
	public void set_sex(String _sex) {
		this._sex = Gender.valueOf(_sex);
	}

	/**
	 * @return the _tel
	 */
	public String get_tel() {
		return _tel;
	}

	/**
	 * @param _tel the _tel to set
	 */
	public void set_tel(String _tel) {
		this._tel = _tel;
	}

	/**
	 * @return the _id
	 */
	public int get_id() {
		return _id;
	}

	public void set_id(int id) {
		this._id = id;
	}

	/**
	 * @return the _permission
	 */
	public int get_permission() {
		return _permission;
	}

}
