package com.webapp.model.user;

public class User {

  private int id;
  private String username;
  private String password;
  private String name;
  private Gender sex;
  private int permission;
  private String tel;

  public User() {
  }

  public User(String username, String password, String name, String sex, String tel) {
    super();
    this.id = 0;
    this.username = username;
    this.password = password;
    this.name = name;
    this.sex = Gender.valueOf(sex);
    this.permission = 1;
    this.tel = tel;
  }

  public User(int id, String username, String password, String name, String sex, String tel) {
    super();
    this.id = id;
    this.username = username;
    this.password = password;
    this.name = name;
    this.sex = Gender.valueOf(sex);
    this.permission = 1;
    this.tel = tel;
  }

  public User(
      int id,
      String username,
      String password,
      String name,
      String sex,
      String tel,
      int permission) {
    super();
    this.id = id;
    this.username = username;
    this.password = password;
    this.name = name;
    this.sex = Gender.valueOf(sex);
    this.permission = permission;
    this.tel = tel;
  }

  public User(int id) {
    this.id = id;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   *     the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   *     the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *     the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the sex
   */
  public Gender getSex() {
    return sex;
  }

  /**
   * @param sex
   *     the sex to set
   */
  public void setSex(String sex) {
    this.sex = Gender.valueOf(sex);
  }

  /**
   * @return the tel
   */
  public String getTel() {
    return tel;
  }

  /**
   * @param tel
   *     the tel to set
   */
  public void setTel(String tel) {
    this.tel = tel;
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the permission
   */
  public int getPermission() {
    return permission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    if (id != user.id) {
      return false;
    }
    if (permission != user.permission) {
      return false;
    }
    if (!username.equals(user.username)) {
      return false;
    }
    if (!password.equals(user.password)) {
      return false;
    }
    if (!name.equals(user.name)) {
      return false;
    }
    if (sex != user.sex) {
      return false;
    }
    return tel.equals(user.tel);
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + username.hashCode();
    result = 31 * result + password.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + sex.hashCode();
    result = 31 * result + permission;
    result = 31 * result + tel.hashCode();
    return result;
  }
}
