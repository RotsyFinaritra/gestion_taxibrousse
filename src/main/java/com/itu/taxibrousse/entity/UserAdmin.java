package com.itu.taxibrousse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_admin")
public class UserAdmin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;
    
    @Column(length = 50)
    private String username;
    
    @Column(length = 50)
    private String password;
    
    // Constructors
    public UserAdmin() {
    }
    
    public UserAdmin(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public Integer getIdUser() {
        return idUser;
    }
    
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
