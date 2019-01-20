package com.franklinux.spring.boot.backend.apirest.controllers;

import com.franklinux.spring.boot.backend.apirest.models.entity.Client;
import com.franklinux.spring.boot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClientRESTController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/clients")
    public List<Client> index(){
        return clientService.findAll();
    }

}
