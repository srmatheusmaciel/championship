package com.matheusmaciel.championship.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(length = 4, nullable = false)
    private String code;

    @Column(length = 2)
    private String state;

    @Column(length = 50)
    private String stadium;

}
