package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupExportImportDTO {

    private String name;
    private List<WordExportDTO> wordsList;
}
