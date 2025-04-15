package com.example.demo.dto;

import com.example.demo.enums.ImportConflictOption;
import lombok.Data;

import java.util.HashMap;

@Data
public class ImportRequestDTO {

    private String data;
    private HashMap<String, ImportConflictOption> conflictResolution;
}
