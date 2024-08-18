package com.example.controller;

import com.example.model.JobApplication;
import com.example.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class JobApplicationController {

    @Autowired
    private JobApplicationService service;

    // Directory to save uploaded files
    private final String UPLOAD_DIR = "D:/JobApplicationForms/";

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("jobApplication", new JobApplication());
        return "job-application-form";
    }

    @PostMapping("/apply")
    public String apply(@RequestParam("name") String name,
                        @RequestParam("email") String email,
                        @RequestParam("phone") String phone,
                        @RequestParam("resume") MultipartFile resume,
                        Model model) {
        // Create a new JobApplication object and set the form fields
        JobApplication application = new JobApplication();
        application.setName(name);
        application.setEmail(email);
        application.setPhone(phone);

        // Process the file upload
        if (!resume.isEmpty()) {
            try {
                // Set file name and type
                String fileName = resume.getOriginalFilename();
                String fileType = resume.getContentType();
                
                // Ensure the upload directory exists
                File uploadDirectory = new File(UPLOAD_DIR);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs(); // Create directory if it does not exist
                }

                // Save file to the specified directory
                File file = new File(uploadDirectory, fileName);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(resume.getBytes());
                }

                // Store file information in the database
                application.setResumeFileName(fileName);
                application.setResumeFileType(fileType);
                application.setResumeData(resume.getBytes()); // Optionally store file data
            } catch (IOException e) {
                e.printStackTrace(); // Handle error accordingly
            }
        }

        // Save the application using the service
        service.saveApplication(application);
        return "application-success";
    }
}
