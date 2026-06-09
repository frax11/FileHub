package com.frax.BackEnd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="shared_files")
public class SharedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private FileEntity file;

    @ManyToOne
    @JoinColumn(name = "shared_with_id")
    private UserEntity sharedWith;

    @Column(name = "shared_at")
    private LocalDateTime sharedAt = LocalDateTime.now();

    @Column(name = "is_active")
    private boolean isActive = true;



}
