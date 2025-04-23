package com.example.demo.dto;

import lombok.Data;

@Data
public class QuizRequestDTO {

    private double rate;
    private int count;
    private Long groupId;
}
