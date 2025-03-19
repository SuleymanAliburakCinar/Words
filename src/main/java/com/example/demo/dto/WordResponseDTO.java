package com.example.demo.dto;

import lombok.Data;

@Data
public class WordResponseDTO {

    private Long id;
    private String name;
    private String mean;
    private int attempt;
    private int correct;
    private GroupDTO group;
}
