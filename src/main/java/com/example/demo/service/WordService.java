package com.example.demo.service;

import com.example.demo.constant.ErrorMessages;
import com.example.demo.dto.*;
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
        if(wordRequestDTO.getGroupId() == null){
            wordRequestDTO.setGroupId(1L);
        }

        if(wordRepository.findByNameAndGroupEntity_Id(wordRequestDTO.getName(), wordRequestDTO.getGroupId()).isPresent()){
            throw new ExistingEntityException(ErrorMessages.ENTITY_ALREADY_EXIST.getMessage());
        }

        return wordMapper.wordEntityToWordResponseDto(wordRepository.save(wordMapper.wordRequestDTOToWordEntity(wordRequestDTO)));
    }

    public void saveImportedWord(List<WordExportDTO> wordExportDTOList, Long groupId){
        List<WordRequestDTO> wordRequestDTOList = wordExportDTOList.stream()
                .map(dto -> {
                    WordRequestDTO wordRequestDTO = wordMapper.wordExportDtoToWordRequestDto(dto);
                    wordRequestDTO.setGroupId(groupId);
                    return wordRequestDTO;
                })
                .toList();

        for(WordRequestDTO word : wordRequestDTOList){
            if(!wordRepository.existsByNameAndGroupEntity_Id(word.getName(), word.getGroupId())) saveWord(word);
        }
    }

    public WordResponseDTO getWordById(Long id){
        return wordMapper.wordEntityToWordResponseDto(wordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage())));
    }

    public List<WordResponseDTO> getAllWords(){
        return wordMapper.wordEntitiesToWordResponseDtos(wordRepository.findAll());
    }

    public WordResponseDTO updateWord(Long id, WordRequestDTO wordRequestDTO){
        WordEntity word = wordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));

        if(!word.getName().equals(wordRequestDTO.getName())) {
            wordRepository.findByNameAndGroupEntity_Id(wordRequestDTO.getName(), wordRequestDTO.getGroupId()).ifPresent(w -> {
                throw new ExistingEntityException(ErrorMessages.ENTITY_ALREADY_EXIST.getMessage());
            });
        }
        word.setName(wordRequestDTO.getName());
        word.setMean(wordRequestDTO.getMean());
        return wordMapper.wordEntityToWordResponseDto(wordRepository.save(word));
    }

    public void deleteWord(Long id){
        wordRepository.deleteById(id);
    }

    private void increaseRateById(Long id){
        WordResponseDTO word = getWordById(id);
        word.setCorrect(word.getCorrect() + 1);
        word.setAttempt(word.getAttempt() + 1);
        wordRepository.save(wordMapper.wordResponseDtoToWordEntity(word));
    }

    private void decreaseRateById(Long id){
        WordResponseDTO word = getWordById(id);
        word.setAttempt(word.getAttempt() + 1);
        wordRepository.save(wordMapper.wordResponseDtoToWordEntity(word));
    }

    private List<WordResponseDTO> getWordListByRateAndCount(QuizRequestDTO quizRequestDTO){
        List<WordEntity> wordList = wordRepository.findByRateGreaterThanEqual(quizRequestDTO.getRate(), quizRequestDTO.getCount());
        return wordList == null ? List.of() : wordMapper.wordEntitiesToWordResponseDtos(wordList);
    }

    private List<WordResponseDTO> getWordListByRateAndCountAndGroupId(QuizRequestDTO quizRequestDTO){
        List<WordEntity> wordList = wordRepository.findByRateGreaterThanEqualAndGroupId(quizRequestDTO.getRate(), quizRequestDTO.getCount(), quizRequestDTO.getGroupId());
        return wordList == null ? List.of() : wordMapper.wordEntitiesToWordResponseDtos(wordList);
    }

    private double getWordListDifficulty(List<WordResponseDTO> wordResponseDTOList){
        double difficulty = 0;
        int count = 0;
        for (WordResponseDTO word : wordResponseDTOList){
            if(word.getAttempt() > 0) {
                count++;
                difficulty += ((double) word.getCorrect() / word.getAttempt());
            }
        }
        return count == 0 ? 100 : (1 - difficulty/count) * 100;
    }

    public QuizQuestionDTO getQuizQuestions(QuizRequestDTO quizRequestDTO){
        QuizQuestionDTO quizQuestionDTO = new QuizQuestionDTO();
        if(quizRequestDTO.getGroupId() != null) {
            quizQuestionDTO.setQuestionList(getWordListByRateAndCountAndGroupId(quizRequestDTO));
        }
        else{
            quizQuestionDTO.setQuestionList(getWordListByRateAndCount(quizRequestDTO));
        }
        quizQuestionDTO.setDifficulty(getWordListDifficulty(quizQuestionDTO.getQuestionList()));
        return quizQuestionDTO;
    }

    public List<WordResponseDTO> getWordListByGroupId(Long groupId){
        List<WordEntity> wordEntityList = wordRepository.findByGroupEntity_Id(groupId);
        return wordEntityList == null ? List.of() : wordMapper.wordEntitiesToWordResponseDtos(wordEntityList);
    }
}
