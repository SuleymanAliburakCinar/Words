package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizReportDTO {

    private List<CardDTO> cards;
    private double successRate;
    private double difficulty;
}
