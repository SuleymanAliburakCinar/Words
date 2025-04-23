package com.example.demo.mapper;

import com.example.demo.dto.WordExportDTO;
import com.example.demo.dto.WordRequestDTO;
import com.example.demo.dto.WordResponseDTO;
import com.example.demo.entity.WordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WordMapper {

    @Mapping(source = "groupEntity.id", target = "groupId")
    WordRequestDTO wordEntityToWordRequestDto(WordEntity wordEntity);
    @Mapping(source = "groupId", target = "groupEntity.id")
    WordEntity wordRequestDTOToWordEntity(WordRequestDTO wordRequestDTO);

    @Mapping(source = "groupEntity.id", target = "groupId")
    WordResponseDTO wordEntityToWordResponseDto(WordEntity word);
    @Mapping(source = "groupId", target = "groupEntity.id")
    WordEntity wordResponseDtoToWordEntity(WordResponseDTO wordResponseDTO);
    List<WordResponseDTO> wordEntitiesToWordResponseDtos(List<WordEntity> wordList);

    WordExportDTO wordResponseDtoToWordExportDto(WordResponseDTO wordResponseDTO);

    WordRequestDTO wordExportDtoToWordRequestDto(WordExportDTO wordExportDTO);
}
