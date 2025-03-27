package com.example.demo.controller;

import com.example.demo.dto.QuizRequestDTO;
import com.example.demo.dto.RequestDTO;
import com.example.demo.dto.WordRequestDTO;
import com.example.demo.dto.WordResponseDTO;
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

    @GetMapping("/{name}")
    public ResponseEntity<WordResponseDTO> getWord(@PathVariable String name){
        return ResponseEntity.ok(wordService.getWordByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WordResponseDTO> updateWord(@PathVariable Long id, @RequestBody WordRequestDTO wordRequestDTO){
        return ResponseEntity.ok(wordService.updateWord(id, wordRequestDTO));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteWord(@PathVariable String name){
        wordService.deleteWord(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/getByRate")
    public ResponseEntity<List<WordResponseDTO>> getByRate(@RequestBody RequestDTO requestDTO){
        return ResponseEntity.ok(wordService.getWordListByRateAndCount(requestDTO));
    }

    @PostMapping("/getByRateAndGroup")
    public ResponseEntity<List<WordResponseDTO>> getByRateAndGroup(@RequestBody RequestDTO requestDTO){
        return ResponseEntity.ok(wordService.getWordListByRateAndCountAndGroupId(requestDTO));
    }

    @PostMapping("/getConclusion")
    public ResponseEntity<String> checkAnswer(@RequestBody QuizRequestDTO quizRequestDTO){
        return ResponseEntity.ok(wordService.getConclusion(quizRequestDTO.getQuestionList(), quizRequestDTO.getAnswerList()));
    }

    @GetMapping("/getByGroupId/{id}")
    public ResponseEntity<List<WordResponseDTO>> getByGroupId(@PathVariable Long id){
        return ResponseEntity.ok(wordService.getWordListByGroupId(id));
    }
}