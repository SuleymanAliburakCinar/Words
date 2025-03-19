package com.example.demo.mapper;

import com.example.demo.dto.GroupDTO;
import com.example.demo.dto.GroupSimpleDTO;
import com.example.demo.dto.WordResponseDTO;
import com.example.demo.entity.GroupEntity;
import com.example.demo.entity.WordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(source = "wordsList", target = "wordEntity")
    GroupEntity groupDtoToGroupEntity(GroupDTO groupDTO);
    @Mapping(source = "wordEntity", target = "wordsList")
    GroupDTO groupEntityToGroupDto(GroupEntity groupEntity);

    List<GroupDTO> groupEntitiesToGroupDtos(List<GroupEntity> groupEntityList);
    List<GroupEntity> groupDtosToGroupEntities(List<GroupDTO> groupEntityList);

    GroupSimpleDTO groupEntityToGroupSimpleDto(GroupEntity groupEntity);
    GroupEntity groupSimpleDtoToGroupEntity(GroupSimpleDTO groupSimpleDTO);

    List<GroupSimpleDTO> groupEntitiesToGroupSimpleDtos(List<GroupEntity> groupEntityList);
    List<GroupEntity> groupSimpleDtosToGroupEntities(List<GroupSimpleDTO> groupSimpleDTOList);

    WordEntity wordResponseDtoToWordEntity(WordResponseDTO wordResponseDTO);
    WordResponseDTO wordEntityToWordResponseDto(WordEntity wordEntity);
}
