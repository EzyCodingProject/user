package com.EzyCoding.user.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
public class Coder {
    @Id
    private UUID coderId;

    @Column(unique = true, length = 100)
    private String email;

    @Column
    private String handle;

    public Coder(UUID coderId) {
        this.coderId = coderId;
    }
}
