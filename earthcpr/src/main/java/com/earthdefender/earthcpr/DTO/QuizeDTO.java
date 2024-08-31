package com.earthdefender.earthcpr.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizeDTO {
    private String question;
    private String answer;
    private List<String> examples;
}
