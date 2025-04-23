package com.example.demo.service;

import com.example.demo.constant.ErrorMessages;
import com.example.demo.dto.GroupDTO;
import com.example.demo.dto.GroupExportImportDTO;
import com.example.demo.dto.GroupSimpleDTO;
import com.example.demo.entity.GroupEntity;
import com.example.demo.exception.ExistingEntityException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupDTO saveSimpleGroup(GroupSimpleDTO groupSimpleDTO){
        if(groupRepository.findByName(groupSimpleDTO.getName()).isPresent()){
            throw new ExistingEntityException(ErrorMessages.ENTITY_ALREADY_EXIST.getMessage());
        }

        return groupMapper.groupEntityToGroupDto(groupRepository.save(groupMapper.groupSimpleDtoToGroupEntity(groupSimpleDTO)));
    }

    public Optional<GroupEntity> saveImportedGroup(GroupExportImportDTO groupExportImportDTO){
        if(existsByName(groupExportImportDTO.getName())){
            return Optional.empty();
        }
        return Optional.of(groupRepository.save(groupMapper.groupExportImportDtoToGroupEntity(groupExportImportDTO)));
    }

    public GroupDTO getGroupByName(String name){
        return groupMapper.groupEntityToGroupDto(groupRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage())));
    }

    public boolean existsByName(String name){
        return groupRepository.existsByName(name);
    }

    public Long getIdByName(String name){
        return groupRepository.findIdByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
    }

    public List<GroupSimpleDTO> getAllGroupSimple(){
        return groupMapper.groupEntitiesToGroupSimpleDtos(groupRepository.findAll());
    }

    public List<GroupDTO> getAllGroup(){
        return groupMapper.groupEntitiesToGroupDtos(groupRepository.findAll());
    }

    public void deleteGroupByName(String name){
        groupRepository.deleteByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
    }

    public GroupDTO updateGroup(String name, String newName){
        GroupEntity groupEntity = groupRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
        groupEntity.setName(newName);
        return groupMapper.groupEntityToGroupDto(groupRepository.save(groupEntity));
    }

    public GroupDTO getGroupById(Long id){
        return groupMapper.groupEntityToGroupDto(groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage())));
    }
}
