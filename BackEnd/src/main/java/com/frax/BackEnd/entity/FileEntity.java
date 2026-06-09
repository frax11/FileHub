package com.frax.BackEnd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
@Entity
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name",nullable = false)
    private String fileName;

    @Column(name = "path",nullable = false)
    private String filePath;

    @Column(name = "size",nullable = false)
    private Long fileSize;

    @Column(name = "type",nullable = false)
    private String fileType;


    // Molti file → un utente (proprietario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",nullable = false)
    private UserEntity owner;  // chi ha caricato il file


    // Un file → molte condivisioni
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharedFile> sharedWithUsers = new ArrayList<>();

    //attributi per condivisione
    private boolean isShared;
    private int maxAccessCount = 100;
    private int currentAccessCount = 0;

    private LocalDateTime uploadDate =  LocalDateTime.now();


}
