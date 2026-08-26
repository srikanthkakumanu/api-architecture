package com.apidemo.rest.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ToDo {
    @NotNull private String id;
    @NotNull @NotBlank private String description;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean completed;

    public ToDo() {
        LocalDateTime date = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
        this.created = date;
        this.modified = date;
    }

    public ToDo(String description) {
        this();
        this.description = description;
    }
}
