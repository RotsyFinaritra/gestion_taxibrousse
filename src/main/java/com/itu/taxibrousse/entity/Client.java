package com.itu.taxibrousse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "client")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Integer idClient;
    
    @Column(length = 50)
    private String username;
    
    @Column(length = 50)
    private String password;
    
    // Constructors
    public Client() {
    }
    
    public Client(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public Integer getIdClient() {
        return idClient;
    }
    
    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
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
