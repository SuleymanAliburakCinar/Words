package com.example.demo.controller;

import com.example.demo.dto.ImportConflictResponseDTO;
import com.example.demo.dto.ImportRequestDTO;
import com.example.demo.service.ImportExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class ImportExportController {

    private final ImportExportService importExportService;

    @GetMapping("/export/all")
    public ResponseEntity<String> exportAllGroups(){
        return ResponseEntity.ok(importExportService.exportAllGroupsAsJson());
    }

    @PostMapping("/export/selected")
    public ResponseEntity<String> exportGroups(@RequestBody List<Long> ids){
        return ResponseEntity.ok(importExportService.exportGroupsAsJson(ids));
    }

    @GetMapping("/export/{id}")
    public ResponseEntity<String> exportGroup(@PathVariable Long id){
        return ResponseEntity.ok(importExportService.exportGroupAsJson(id));
    }

    @PostMapping("/import")
    public ResponseEntity<ImportConflictResponseDTO> importGroup(@RequestBody String encodedJson){
        return ResponseEntity.ok(importExportService.checkImportConflictAndSave(encodedJson));
    }

    @PostMapping("/import/decision")
    public ResponseEntity<String> importDecidedData(@RequestBody ImportRequestDTO importRequestDTO){
        return ResponseEntity.ok(importExportService.importDecidedData(importRequestDTO));
    }
}
