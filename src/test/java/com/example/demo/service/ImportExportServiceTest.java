package com.example.demo.service;

import com.example.demo.dto.GroupExportImportDTO;
import com.example.demo.dto.ImportConflictResponseDTO;
import com.example.demo.entity.GroupEntity;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.util.ImportExportServiceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.Validator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportExportServiceTest {

    @Mock
    private GroupService groupService;

    @Mock
    private WordService wordService;

    @Spy
    private ObjectMapper objectMapper;

    @Spy
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    @Spy
    private Validator validator = validatorFactory.getValidator();

    @Spy
    private GroupMapper groupMapper;

    @InjectMocks
    private ImportExportService importExportService;

    @Test
    void checkImportConflictAndSave_shouldReturnConflictResponse_whenSomeGroupsConflict() {
        List<GroupExportImportDTO> groupList = ImportExportServiceUtil.getMultipleGroupDTO();

        String json = ImportExportServiceUtil.encodeToBase64(groupList);

        when(groupService.saveImportedGroup(groupList.get(0))).thenReturn(Optional.of(new GroupEntity()));
        when(groupService.saveImportedGroup(groupList.get(1))).thenReturn(Optional.empty());

        ImportConflictResponseDTO result = importExportService.checkImportConflictAndSave(json);

        assertEquals(1, result.getGroupNameList().size());
        assertTrue(result.getGroupNameList().contains(groupList.get(1).getName()));
    }

    @Test
    void checkImportConflictAndSave_shouldThrowException_whenGroupNameIsBlank() {
        GroupExportImportDTO groupWithBlankName = ImportExportServiceUtil.createGroupWithName("  ");
        String json = ImportExportServiceUtil.encodeToBase64(groupWithBlankName);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> importExportService.checkImportConflictAndSave(json));
        assertEquals("Import data is incorrect", ex.getMessage());
    }

    @Test
    void checkImportConflictAndSave_shouldThrowException_whenGroupNameIsNull() {
        GroupExportImportDTO groupDTO = ImportExportServiceUtil.createGroupWithName("");
        groupDTO.setName(null);
        String json = ImportExportServiceUtil.encodeToBase64(groupDTO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> importExportService.checkImportConflictAndSave(json));
        assertEquals("Import data is incorrect", ex.getMessage());
    }

    @Test
    void checkImportConflictAndSave_shouldNotThrowException_whenDtoValid() {
        GroupExportImportDTO validGroup = ImportExportServiceUtil.createGroupWithName("name");

        String json = ImportExportServiceUtil.encodeToBase64(validGroup);

        assertDoesNotThrow(() -> importExportService.checkImportConflictAndSave(json));
    }
}
