package com.example.demo.service;

import com.example.demo.dto.GroupDTO;
import com.example.demo.dto.GroupExportImportDTO;
import com.example.demo.dto.ImportConflictResponseDTO;
import com.example.demo.dto.ImportRequestDTO;
import com.example.demo.mapper.GroupMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ImportExportService {

    private final GroupService groupService;
    private final GroupMapper groupMapper;
    private final ObjectMapper objectMapper;
    private final WordService wordService;

    public String exportAllGroupsAsJson(){
        List<GroupDTO> groupDTOList = groupService.getAllGroup();
        List<GroupExportImportDTO> groupExportImportDTOList = groupDTOList.stream()
                .map(groupMapper::groupDtoToGroupExportImportDto)
                .toList();

        return encodeToString(groupExportImportDTOList);
    }

    public String exportGroupsAsJson(List<Long> groupIds){
        List<GroupExportImportDTO> groupExportImportDTOList = new ArrayList<>();
        for(Long id : groupIds){
            groupExportImportDTOList.add(groupMapper.groupDtoToGroupExportImportDto(groupService.getGroupById(id)));
        }

        return encodeToString(groupExportImportDTOList);
    }

    public String exportGroupAsJson(Long groupId){
        GroupDTO group = groupService.getGroupById(groupId);
        GroupExportImportDTO groupExportImportDTO = groupMapper.groupDtoToGroupExportImportDto(group);

        return encodeToString(groupExportImportDTO);
    }

    public ImportConflictResponseDTO checkImportConflictAndSave(String encodedJson){
        List<GroupExportImportDTO> importData = decodeToDTO(encodedJson);
        List<GroupExportImportDTO> conflictingData = new ArrayList<>();
        ImportConflictResponseDTO importConflictResponseDTO = new ImportConflictResponseDTO();
        for(GroupExportImportDTO group : importData){
            groupService.saveImportedGroup(group).ifPresentOrElse(
                    addedGroup -> wordService.saveImportedWord(group.getWordsList(),addedGroup.getId()),
                    () -> {
                        conflictingData.add(group);
                        importConflictResponseDTO.addGroupName(group.getName());
                    }
            );
        }
        importConflictResponseDTO.setJson(encodeToString(conflictingData));
        importConflictResponseDTO.setMsg(
                String.format(
                        "%d groups added, %d conflicts detected. Please resolve the conflicts.",
                        (importData.size() - importConflictResponseDTO.getGroupNameList().size()),
                        importConflictResponseDTO.getGroupNameList().size()
                ));
        return importConflictResponseDTO;
    }

    public String importDecidedData(ImportRequestDTO importRequestDTO){
        List<GroupExportImportDTO> importData = decodeToDTO(importRequestDTO.getData());
        int errorCount = 0;
        int successCount = 0;
        for(GroupExportImportDTO group : importData){
            switch (importRequestDTO.getConflictResolution().get(group.getName())){
                case RENAME -> {
                    group.setName(String.format("%s (%d)", group.getName(), Instant.now().getEpochSecond()));
                    groupService.saveImportedGroup(group).ifPresent(addedGroup -> wordService.saveImportedWord(group.getWordsList(), addedGroup.getId()));
                    successCount++;
                }
                case MERGE -> {
                    wordService.saveImportedWord(group.getWordsList(), groupService.getIdByName(group.getName()));
                    successCount++;
                }
                case IGNORE -> successCount++;
                default -> errorCount++;
            }
        }

        return String.format("%d data were imported successfully. %d Error", successCount, errorCount);
    }

    private List<GroupExportImportDTO> decodeToDTO(String encodedJson){
        try {
            String json = new String(Base64.getDecoder().decode(encodedJson.getBytes(StandardCharsets.UTF_8)));
            JsonNode jsonNode = objectMapper.readTree(json);

            if(jsonNode.isArray()) {
                return objectMapper.readValue(json, new TypeReference<>() {});
            } else if(jsonNode.isObject()) {
                return List.of(objectMapper.readValue(json, GroupExportImportDTO.class));
            }
            return Collections.emptyList();
        }
        catch (JsonProcessingException e){
            throw new RuntimeException("JSON Decode Error", e);
        }
    }

    private String encodeToString(Object object){
        try {
            String json = objectMapper.writeValueAsString(object);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("JSON Encode Error", e);
        }
    }
}
