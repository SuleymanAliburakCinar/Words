package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/words")
public class WordController {

    private final WordService wordService;

    @PostMapping
    public ResponseEntity<WordResponseDTO> saveWord(@RequestBody WordRequestDTO wordRequestDTO){
        return ResponseEntity.ok(wordService.saveWord(wordRequestDTO));
    }

    @GetMapping
    public List<WordResponseDTO> getAllWords(){
        return wordService.getAllWords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WordResponseDTO> getWord(@PathVariable Long id){
        return ResponseEntity.ok(wordService.getWordById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WordResponseDTO> updateWord(@PathVariable Long id, @RequestBody WordRequestDTO wordRequestDTO){
        return ResponseEntity.ok(wordService.updateWord(id, wordRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWord(@PathVariable Long id){
        wordService.deleteWord(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/quiz")
    public ResponseEntity<QuizQuestionDTO> getByRate(@RequestBody QuizRequestDTO quizRequestDTO){
        return ResponseEntity.ok(wordService.getQuizQuestions(quizRequestDTO));
    }


    @GetMapping("/getByGroupId/{id}")
    public ResponseEntity<List<WordResponseDTO>> getByGroupId(@PathVariable Long id){
        return ResponseEntity.ok(wordService.getWordListByGroupId(id));
    }

    @PostMapping("/quiz/answer")
    public ResponseEntity<Void> processAnswers(@RequestBody List<QuizAnswersDTO> quizAnswersDTOList){
        wordService.processAnswers(quizAnswersDTOList);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/groupInfo/{groupId}")
    public ResponseEntity<GroupInfoProjection> getGroupInfo(@PathVariable Long groupId){
        return ResponseEntity.ok(wordService.getGroupInfo(groupId));
    }
}