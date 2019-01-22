package com.franklinux.spring.boot.backend.apirest.models.dao;

import com.franklinux.spring.boot.backend.apirest.models.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

//  Extend of JpaRepository to implement the pagination
public interface IClientDAO extends JpaRepository<Client, Long> {


}
