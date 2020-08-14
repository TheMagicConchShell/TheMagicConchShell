package com.blsa.ezilog.model.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "en_name")
    private String enName;

    @Column(name = "ch_name")
    private String chName;

    public Category(String name, String description, String enName, String chName) {
        super();
        this.name = name;
        this.description = description;
        this.enName = enName;
        this.chName = chName;
    }

}
