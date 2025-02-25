package com.example.ticket_support.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequest {
    private String text;
}
