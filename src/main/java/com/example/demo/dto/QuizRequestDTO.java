package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizRequestDTO {

    private List<String> questionList;
    private List<String> answerList;
}
