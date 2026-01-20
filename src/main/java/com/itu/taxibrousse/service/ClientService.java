package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Client;
import com.itu.taxibrousse.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {
    
    private final ClientRepository clientRepository;
    
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
    
    public Optional<Client> findById(Integer id) {
        return clientRepository.findById(id);
    }
    
    public Client save(Client client) {
        return clientRepository.save(client);
    }
    
    public void deleteById(Integer id) {
        clientRepository.deleteById(id);
    }
    
    public Optional<Client> findByUsername(String username) {
        return clientRepository.findByUsername(username);
    }
    
    public boolean existsByUsername(String username) {
        return clientRepository.existsByUsername(username);
    }
}
