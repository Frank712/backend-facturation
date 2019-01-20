package com.franklinux.spring.boot.backend.apirest.controllers;

import com.franklinux.spring.boot.backend.apirest.models.entity.Client;
import com.franklinux.spring.boot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClientRESTController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/clients")
    public List<Client> index(){
        return clientService.findAll();
    }

    @GetMapping("clients/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Client show(@PathVariable Long id){
        return clientService.findByID( id );
    }

    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public Client create( @RequestBody Client client ){
        return clientService.save( client );
    }

    @PutMapping("/clients/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Client update(@RequestBody Client client, @PathVariable Long id) {
        //  Get the current client in database
        Client currentClient = clientService.findByID(id);
        //  Modify the data current client
        currentClient.setName(client.getName());
        currentClient.setLastname(client.getLastname());
        currentClient.setEmail(client.getEmail());

        return clientService.save( currentClient );
    }

    @DeleteMapping("/clients/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete( @PathVariable Long id ){
        clientService.delete(id);
    }
}
