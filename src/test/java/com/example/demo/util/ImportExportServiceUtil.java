package com.example.demo.util;

import com.example.demo.dto.GroupExportImportDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class ImportExportServiceUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static GroupExportImportDTO createGroupWithName(String name) {
        return new GroupExportImportDTO(name, List.of());
    }

    public static String encodeToBase64(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Test JSON encode failed", e);
        }
    }

    public static List<GroupExportImportDTO> getMultipleGroupDTO() {
        GroupExportImportDTO firstGroup = createGroupWithName("first group");
        GroupExportImportDTO secondGroup = createGroupWithName("second group");
        return List.of(firstGroup, secondGroup);
    }

}
