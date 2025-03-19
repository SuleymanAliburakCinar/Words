package com.example.demo.mapper;

import com.example.demo.dto.GroupDTO;
import com.example.demo.dto.WordRequestDTO;
import com.example.demo.dto.WordResponseDTO;
import com.example.demo.entity.GroupEntity;
import com.example.demo.entity.WordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WordMapper extends GenericMapper<WordResponseDTO, WordEntity> {

    @Mapping(source = "groupEntity", target = "group")
    WordRequestDTO wordEntityToWordRequestDto(WordEntity wordEntity);
    @Mapping(source = "group", target = "groupEntity")
    WordEntity wordRequestDTOToWordEntity(WordRequestDTO wordRequestDTO);

    GroupDTO groupEntityToGroupDto(GroupEntity groupEntity);
    GroupEntity groupDtoToGroupEntity(GroupDTO groupDTO);
}
