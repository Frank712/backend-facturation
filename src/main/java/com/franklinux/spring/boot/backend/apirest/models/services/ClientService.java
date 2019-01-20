package com.franklinux.spring.boot.backend.apirest.models.services;

import com.franklinux.spring.boot.backend.apirest.models.dao.IClientDAO;
import com.franklinux.spring.boot.backend.apirest.models.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService implements IClientService {

    @Autowired
    private IClientDAO clientDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return (List<Client>) clientDAO.findAll();
    }

    @Override
    @Transactional
    public Client findByID(Long id) {
        //  The client not found, return null
        return clientDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Client save(Client client) {
        return clientDAO.save(client);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clientDAO.deleteById(id);
    }
}
