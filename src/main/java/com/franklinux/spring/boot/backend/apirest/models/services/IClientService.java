package com.franklinux.spring.boot.backend.apirest.models.services;

import com.franklinux.spring.boot.backend.apirest.models.entity.Client;

import java.util.List;

public interface IClientService {
    public List<Client> findAll();
}
