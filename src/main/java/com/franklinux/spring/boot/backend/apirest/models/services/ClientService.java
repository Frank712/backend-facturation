package com.franklinux.spring.boot.backend.apirest.models.services;

import com.franklinux.spring.boot.backend.apirest.models.dao.IClientDAO;
import com.franklinux.spring.boot.backend.apirest.models.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ClientService implements IClientService {

    @Autowired
    private IClientDAO clientDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return (List<Client>) clientDAO.findAll();
    }
}
