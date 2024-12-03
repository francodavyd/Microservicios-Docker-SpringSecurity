package com.francodavyd.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EmailDTO {
    private String[] toUser;
    private String subject;
    private String message;
}