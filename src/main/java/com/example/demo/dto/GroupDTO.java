package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDTO {

    private Long id;
    private String name;
    private List<WordResponseDTO> wordsList;
}
