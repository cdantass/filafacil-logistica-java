package com.apidevops.dev.repository;


import com.apidevops.dev.model.JanelaColeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JanelaRepository
        extends JpaRepository<JanelaColeta, Long> {
}