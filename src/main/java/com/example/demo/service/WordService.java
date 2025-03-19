package com.example.demo.service;

import com.example.demo.constant.ErrorMessages;
import com.example.demo.dto.GroupDTO;
import com.example.demo.dto.RequestDTO;
import com.example.demo.dto.WordRequestDTO;
import com.example.demo.dto.WordResponseDTO;
import com.example.demo.entity.WordEntity;
import com.example.demo.exception.ExistingEntityException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.WordMapper;
import com.example.demo.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final WordMapper wordMapper;

    public WordResponseDTO saveWord(WordRequestDTO wordRequestDTO){
        if(wordRepository.findByName(wordRequestDTO.getName()).isPresent()){
            throw new ExistingEntityException(ErrorMessages.ENTITY_ALREADY_EXIST.getMessage());
        }

        if(wordRequestDTO.getGroup() == null){
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setId(Long.parseLong("1"));
            groupDTO.setName("Vocabulary");
            wordRequestDTO.setGroup(groupDTO);
        }
        return wordMapper.mapToDto(wordRepository.save(wordMapper.wordRequestDTOToWordEntity(wordRequestDTO)));
    }

    public WordResponseDTO getWordByName(String name){
        return wordMapper.mapToDto(wordRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage())));
    }

    public List<WordResponseDTO> getAllWords(){
        return wordMapper.mapListToDto(wordRepository.findAll());
    }

    public WordResponseDTO updateWord(WordRequestDTO wordRequestDTO){
        WordEntity word = wordRepository.findByName(wordRequestDTO.getName())
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
        word.setName(wordRequestDTO.getName());
        word.setMean(wordRequestDTO.getMean());
        return wordMapper.mapToDto(wordRepository.save(word));
    }

    public void deleteWord(String name){
        wordRepository.deleteByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
    }

    public void increaseRateByName(String name){
        WordEntity wordEntity = getWordEntityByName(name);
        wordEntity.setCorrect(wordEntity.getCorrect() + 1);
        wordEntity.setAttempt(wordEntity.getAttempt() + 1);
        wordRepository.save(wordEntity);
    }

    public void decreaseRateByName(String name){
        WordEntity wordEntity = getWordEntityByName(name);
        wordEntity.setAttempt(wordEntity.getAttempt() + 1);
        wordRepository.save(wordEntity);
    }

    private WordEntity getWordEntityByName(String name){
        return wordRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
    }

    public List<WordResponseDTO> getWordListByRateAndCount(RequestDTO requestDTO){
        List<WordEntity> wordList = wordRepository.findByRateGreaterThanEqual(requestDTO.getRate(), requestDTO.getCount());
        return wordList == null ? List.of() : wordMapper.mapListToDto(wordList);
    }

    public List<WordResponseDTO> getWordListByRateAndCountAndGroupId(RequestDTO requestDTO){
        List<WordEntity> wordList = wordRepository.findByRateGreaterThanEqualAndGroupId(requestDTO.getRate(), requestDTO.getCount(), requestDTO.getGroupId());
        return wordList == null ? List.of() : wordMapper.mapListToDto(wordList);
    }

    public Boolean checkAnwser(WordRequestDTO wordRequestDTO){
        WordEntity wordEntity = getWordEntityByName(wordRequestDTO.getName());
        if(wordEntity.getMean().equals(wordRequestDTO.getMean())) {
            increaseRateByName(wordEntity.getName());
            return true;
        }
        else{
            decreaseRateByName(wordEntity.getName());
            return false;
        }
    }

    public List<WordResponseDTO> getWordListByGroupId(Long id){
        List<WordEntity> wordEntityList = wordRepository.findByGroupEntity_Id(id);
        return wordEntityList == null ? List.of() : wordMapper.mapListToDto(wordEntityList);
    }
}
