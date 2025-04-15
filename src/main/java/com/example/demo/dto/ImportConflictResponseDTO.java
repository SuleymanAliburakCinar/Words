package com.example.demo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImportConflictResponseDTO {

    private String json;
    private List<String> groupNameList = new ArrayList<>();

    public void addGroupName(String groupName){
        getGroupNameList().add(groupName);
    }
}
