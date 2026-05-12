/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author hp
 */
public class User {

  
    public User(String email, String passwordHash) {

    this.email = email;

    this.passwordHash = passwordHash;
}
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;

   
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public String getFirstName() {
        return firstName;
    }

   
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

   
    public String getLastName() {
        return lastName;
    }

    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
    public String getEmail() {
        return email;
    }

   
    public void setEmail(String email) {
        this.email = email;
    }

   
    public String getPasswordHash() {
        return passwordHash;
    }

   
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
