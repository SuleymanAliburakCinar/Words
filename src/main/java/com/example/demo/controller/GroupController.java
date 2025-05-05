package com.example.demo.controller;

import com.example.demo.dto.GroupDTO;
import com.example.demo.dto.GroupSimpleDTO;
import com.example.demo.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDTO> addGroup(@RequestBody GroupSimpleDTO groupSimpleDTO){
        return ResponseEntity.ok(groupService.saveSimpleGroup(groupSimpleDTO));
    }

    @GetMapping
    public List<GroupSimpleDTO> getAllGroup(){
        return groupService.getAllGroupSimple();
    }

    @GetMapping("/{name}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable String name){
        return ResponseEntity.ok(groupService.getGroupByName(name));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String name){
        groupService.deleteGroupByName(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulkDelete")
    public ResponseEntity<Void> bulkDeleteGroup(@RequestBody List<Long> groupIdList){
        groupService.bulkDelete(groupIdList);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{name}/{newName}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable String name, @PathVariable String newName){
        return ResponseEntity.ok(groupService.updateGroup(name, newName));
    }
}
