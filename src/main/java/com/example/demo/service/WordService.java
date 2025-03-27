package com.example.demo.service;

import com.example.demo.constant.ErrorMessages;
import com.example.demo.dto.*;
import com.example.demo.entity.WordEntity;
import com.example.demo.exception.ExistingEntityException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.WordMapper;
import com.example.demo.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public WordResponseDTO updateWord(Long id, WordRequestDTO wordRequestDTO){
        WordEntity word = wordRepository.findById(id)
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

    public String getConclusion(List<String> questionList, List<String> answerList){
        List<String> wrongAnsweredQuestions = new ArrayList<>();
        List<String> yourAnswers = new ArrayList<>();
        List<String> correctAnswers = new ArrayList<>();
        int mistakeCount = 0;
        for (int i = 0; i < questionList.size(); i++) {
            if (Boolean.FALSE.equals(checkAnswer(questionList.get(i), answerList.get(i)))) {
                increaseRateByName(questionList.get(i));
                wrongAnsweredQuestions.add(questionList.get(i));
                yourAnswers.add(answerList.get(i));
                correctAnswers.add(getMeanByName(questionList.get(i)));
                mistakeCount++;
            }
            else {
                decreaseRateByName(questionList.get(i));
            }
        }
        return getQuizReport(wrongAnsweredQuestions, yourAnswers, correctAnswers,
                mistakeCount == questionList.size() ? 0d : (100d * (questionList.size()-mistakeCount) / questionList.size()));
    }

    private Boolean checkAnswer(String question, String answer){
        WordEntity wordEntity = wordRepository.findByName(question)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
        return wordEntity.getMean().equals(answer);
    }

    private String getQuizReport(List<String> wrongAnsweredQuestions, List<String> yourAnswers, List<String> correctAnswers,
                                 double successRate){
        JSONObject quizReportJA = new JSONObject();
        quizReportJA.put("Wrong Answered Question", new JSONArray(wrongAnsweredQuestions));
        quizReportJA.put("Your Answer", new JSONArray(yourAnswers));
        quizReportJA.put("Correct Answer", new JSONArray(correctAnswers));
        quizReportJA.put("Success Rate", successRate);
        return quizReportJA.toString();
    }

    private String getMeanByName(String name){
        WordEntity wordEntity = wordRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
        return wordEntity.getMean();
    }

    public List<WordResponseDTO> getWordListByGroupId(Long id){
        List<WordEntity> wordEntityList = wordRepository.findByGroupEntity_Id(id);
        return wordEntityList == null ? List.of() : wordMapper.mapListToDto(wordEntityList);
    }
}
