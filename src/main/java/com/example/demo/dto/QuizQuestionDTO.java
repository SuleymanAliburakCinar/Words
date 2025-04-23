package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizQuestionDTO {

    private List<WordResponseDTO> questionList;
    private Double difficulty;
}
