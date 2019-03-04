package com.example.adi.qrscanner;

public class Users{
  public String id,email,password,userID,name;

  public Users(String id, String email, String password, String userID, String name) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.userID = userID;
    this.name = name;
  }

  public Users() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}