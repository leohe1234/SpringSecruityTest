package com.createbest.secruity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
//@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@GenericGenerator(name = "system-uuid", strategy = "uuid")
public class TestB{
    @Id
    //@GeneratedValue(generator = "jpa-uuid")
    @GeneratedValue(generator = "system-uuid")   
    @Column(length = 32)
    private String uid;
    private String name;
    public TestB(){
 
    }
    public String getUid() {
        return uid;
    }
 
    public void setUid(String uid) {
        this.uid = uid;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
}