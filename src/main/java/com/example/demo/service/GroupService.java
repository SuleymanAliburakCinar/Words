package com.example.demo.service;

import com.example.demo.constant.ErrorMessages;
import com.example.demo.dto.GroupDTO;
import com.example.demo.dto.GroupSimpleDTO;
import com.example.demo.entity.GroupEntity;
import com.example.demo.exception.ExistingEntityException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.GroupMapper;
import com.example.demo.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupDTO saveGroup(GroupSimpleDTO groupSimpleDTO){
        if(groupRepository.findByName(groupSimpleDTO.getName()).isPresent()){
            throw new ExistingEntityException(ErrorMessages.ENTITY_ALREADY_EXIST.getMessage());
        }

        return groupMapper.groupEntityToGroupDto(groupRepository.save(groupMapper.groupSimpleDtoToGroupEntity(groupSimpleDTO)));
    }

    public GroupDTO getGroupByName(String name){
        return groupMapper.groupEntityToGroupDto(groupRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage())));
    }

    public List<GroupSimpleDTO> getAllGroup(){
        return groupMapper.groupEntitiesToGroupSimpleDtos(groupRepository.findAll());
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
}
