package com.example.demo.dto;

import lombok.Data;

@Data
public class WordRequestDTO {

    private String name;
    private String mean;
    private GroupDTO group;
}
