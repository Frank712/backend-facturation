package com.franklinux.spring.boot.backend.apirest.models.dao;

import com.franklinux.spring.boot.backend.apirest.models.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface IClientDAO extends CrudRepository<Client, Long> {


}
