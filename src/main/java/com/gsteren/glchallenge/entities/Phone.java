package com.gsteren.glchallenge.entities;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long number;
    private int citycode;
    private String countrycode;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName="id")
    @JsonIgnore
    private User user;

    // Getters and setters

    // Constructors
}
