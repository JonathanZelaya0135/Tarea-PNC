package org.pnc.tarea.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserPostsDto {

    private UUID id;
    private String message;
}