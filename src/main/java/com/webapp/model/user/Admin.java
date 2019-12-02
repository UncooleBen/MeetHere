package com.webapp.model.user;

public class Admin extends User {

	private int _id;
	private String _username;
	private String _password;
	private String _name;
	private Gender _sex;
	private int _permission;
	private String _tel;

	public Admin(int id, String username, String password, String name, String sex, String tel) {
		super();
		this._id = id;
		this._username = username;
		this._password = password;
		this._name = name;
		this._sex = Gender.valueOf(sex);
		this._tel = tel;
	}

	/**
	 * @return the _id
	 */
	@Override
	public int get_id() {
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	@Override
	public void set_id(int _id) {
		this._id = _id;
	}

	/**
	 * @return the _username
	 */
	@Override
	public String get_username() {
		return _username;
	}

	/**
	 * @param _username the _username to set
	 */
	@Override
	public void set_username(String _username) {
		this._username = _username;
	}

	/**
	 * @return the _password
	 */
	@Override
	public String get_password() {
		return _password;
	}

	/**
	 * @param _password the _password to set
	 */
	@Override
	public void set_password(String _password) {
		this._password = _password;
	}

	/**
	 * @return the _name
	 */
	@Override
	public String get_name() {
		return _name;
	}

	/**
	 * @param _name the _name to set
	 */
	@Override
	public void set_name(String _name) {
		this._name = _name;
	}

	/**
	 * @return the _sex
	 */
	@Override
	public Gender get_sex() {
		return _sex;
	}

	/**
	 * @param _sex the _sex to set
	 */
	public void set_sex(Gender _sex) {
		this._sex = _sex;
	}

	/**
	 * @return the _permission
	 */
	@Override
	public int get_permission() {
		return _permission;
	}

	/**
	 * @param _permission the _permission to set
	 */
	public void set_permission(int _permission) {
		this._permission = _permission;
	}

	/**
	 * @return the _tel
	 */
	@Override
	public String get_tel() {
		return _tel;
	}

	/**
	 * @param _tel the _tel to set
	 */
	@Override
	public void set_tel(String _tel) {
		this._tel = _tel;
	}

}
