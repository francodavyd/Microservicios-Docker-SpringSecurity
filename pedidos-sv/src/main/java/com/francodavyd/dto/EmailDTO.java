package com.francodavyd.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmailDTO {
    private String[] toUser;
    private String subject;
    private String message;
}
