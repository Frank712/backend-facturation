package com.franklinux.spring.boot.backend.apirest.controllers;

import com.franklinux.spring.boot.backend.apirest.models.entity.Client;
import com.franklinux.spring.boot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> show(@PathVariable Long id){
        Client client = null;
        Map<String, Object> response = new HashMap<>();

        try {
            client = clientService.findByID( id );

        } catch( DataAccessException e) {
            response.put("message", "An error has ocurred on database");
            response.put("error", e.getMessage().concat(": ").concat( e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if( client == null ){
            response.put("message", "The client ID: ".concat( id.toString() ).concat( " not found!"));
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(client, HttpStatus.OK);
    }

    @PostMapping("/clients")
    public ResponseEntity<?> create( @RequestBody Client client ){
        Client newClient = null;
        Map<String, Object> response = new HashMap<>();
        try {
            newClient = clientService.save( client );
        } catch ( DataAccessException e ) {
            response.put("message", "Error while try to save on database");
            response.put("error", e.getMessage().concat(": ").concat( e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "The client ".concat( newClient.getName() ).concat( " has been created successfully" ) );
        response.put( "client", newClient );
        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<?> update(@RequestBody Client client, @PathVariable Long id) {
        Client currentClient = null;
        Client clientSaved = null;
        Map<String, Object> response = new HashMap<>();
        //  Try to get the current client in database
        try {
            currentClient = clientService.findByID(id);
        } catch ( DataAccessException e ) {
            response.put("message", "Error while try to search user on database");
            response.put("error", e.getMessage().concat(": ").concat( e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if( currentClient == null ){
            response.put("message", "Client ID: ".concat(id.toString()).concat( " not found!"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        //  Modify the data current client
        currentClient.setName(client.getName());
        currentClient.setLastname(client.getLastname());
        currentClient.setEmail(client.getEmail());
        try {
            clientSaved = clientService.save( currentClient );
        } catch( DataAccessException e ){
            response.put("message", "Error while try to salve user on database");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Client ID: ".concat(id.toString()).concat(" has been updated successfully!"));
        response.put("client", clientSaved);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<?> delete( @PathVariable Long id ){
        Map<String, Object> response = new HashMap<>();

        try {
            clientService.delete(id);
        }
        catch ( DataAccessException e) {
            response.put( "message", "Error while try to delete a user from database" );
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "The client ID: ".concat(id.toString()).concat(" has been deleted!"));
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
