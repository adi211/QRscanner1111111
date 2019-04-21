package com.example.adi.qrscanner;

public class Users{
  public String id,email,password,userID,name,imageUrl;
  public Users(String id, String email, String url, String userID, String name) {
    this.id = id;
    this.email = email;
    this.userID = userID;
    this.name = name;
    this.imageUrl=url;
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


  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}