package com.example.service;

import com.example.model.JobApplication;
import com.example.repository.JobApplicationRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository repository;

    public JobApplication saveApplication(JobApplication application) {
        return repository.save(application);
    }

    public List<JobApplication> getAllApplications() {
        return repository.findAll(); // Corrected field name
    }

    public void deleteApplication(Integer id) {
        try {
            repository.deleteById(id); // Use the correct repository field
        } catch (EmptyResultDataAccessException e) {
            // Handle exception or log as needed
            throw e;
        }
    }
}
