package com.franklinux.spring.boot.backend.apirest.controllers;

import com.franklinux.spring.boot.backend.apirest.models.entity.Client;
import com.franklinux.spring.boot.backend.apirest.models.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("/clients/page/{page}")
    public Page<Client> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return clientService.findAll( pageable );
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
    public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result){
        Client newClient = null;
        Map<String, Object> response = new HashMap<>();
        //  Verify if the result has errors
        if( result.hasErrors() ){
            List<String> errors = result.getFieldErrors().stream()
                    .map( error -> "The field '" + error.getField() + "' "  + error.getDefaultMessage() )
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            newClient = clientService.save( client );
        } catch ( DataAccessException e ) {
            response.put("message", "Error while try to save on database");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "The client ".concat( newClient.getName() ).concat( " has been created successfully" ) );
        response.put( "client", newClient );
        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id ) {
        Client currentClient = null;
        Client clientSaved = null;
        Map<String, Object> response = new HashMap<>();
        //  Verify if the result has errors
        if( result.hasErrors() ){
            List<String> errors = result.getFieldErrors().stream()
                    .map( error -> "The field '" + error.getField() + "' "  + error.getDefaultMessage() )
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        //  Try to get the current client in database
        try {
            currentClient = clientService.findByID(id);
        } catch ( DataAccessException e ) {
            response.put("message", "Error while try to search user on database");
            response.put("error", e.getMostSpecificCause().getMessage());
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
        currentClient.setCreatedAt( client.getCreatedAt());
        try {
            clientSaved = clientService.save( currentClient );
        } catch( DataAccessException e ){
            response.put("message", "Error while try to salve user on database");
            //response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Client ID: ".concat(id.toString()).concat(" has been updated successfully!"));
        response.put("client", clientSaved);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<?> delete( @PathVariable Long id ){
        Map<String, Object> response = new HashMap<>();
        Client client = clientService.findByID(id);
        String messageDel = null;
        try {
            String nameFileOld = client.getImg();
            messageDel = deletedOldImg( nameFileOld ) ? "The old image has been deleted" : "The old image can not deleted";
            clientService.delete(id);
        }
        catch ( DataAccessException e) {
            response.put( "message", "Error while try to delete a user from database" );
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "The client ID: ".concat(id.toString()).concat(" has been deleted!"));
        response.put("deleted", messageDel);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/clients/upload")
    public ResponseEntity<?> upload(@RequestParam MultipartFile file, @RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        Client client = clientService.findByID(id);

        if( !file.isEmpty() ){
            String fileName =  id.toString() + "_" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "");
            Path pathFile = Paths.get("uploads").resolve(fileName).toAbsolutePath();
            try {
                Files.copy(file.getInputStream(), pathFile);
            } catch ( IOException e){
                e.printStackTrace();
                response.put( "message", "Error while try to update user image: " + fileName );
                response.put("error", e.getCause().getMessage());
                return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String nameFileOld = client.getImg();
            String messageDel = deletedOldImg( nameFileOld ) ? "The old image has been deleted" : "The old image can not deleted";
            client.setImg( fileName );
            clientService.save(client);
            response.put("client", client);
            response.put("message", "The image " + fileName + " has been uploaded successfully");
            response.put("deleted", messageDel);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Boolean deletedOldImg( String nameFileOld ) {
        if( nameFileOld != null && nameFileOld.length() > 0){
            Path pathImgOld = Paths.get("uploads").resolve(nameFileOld).toAbsolutePath();
            File fileOld = pathImgOld.toFile();
            if( fileOld.exists() && fileOld.canRead() ){
                fileOld.delete();
                return true;
            }
            return false;
        }
        return false;
    }
}
