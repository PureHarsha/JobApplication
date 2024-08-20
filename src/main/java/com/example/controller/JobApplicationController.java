package com.example.controller;

import com.example.model.JobApplication;
import com.example.service.JobApplicationService;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class JobApplicationController {

    @Autowired
    private JobApplicationService service;
    @Autowired
    private ServletContext servletContext; // Injecting ServletContext

    // Directory to save uploaded files
    private final String UPLOAD_DIR = "D:/JobApplicationForms/";
    private static final String ADMIN_PIN = "1234"; // Example PIN for demonstration

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

    @GetMapping("/download")
    public void downloadResume(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
        File file = new File(UPLOAD_DIR + fileName);
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            FileInputStream fis = new FileInputStream(file);
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
            fis.close();
        }
    }
    
    
    @GetMapping("/view-resume")
    public void viewResume(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
        File file = new File(UPLOAD_DIR + fileName);
        if (file.exists()) {
            String mimeType = servletContext.getMimeType(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // Fallback MIME type
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");

            try (FileInputStream fis = new FileInputStream(file)) {
                IOUtils.copy(fis, response.getOutputStream());
                response.flushBuffer();
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
        }
    }



    @GetMapping("/applications")
    public String showApplications(Model model) {
        List<JobApplication> jobApplications = service.getAllApplications();
        model.addAttribute("jobApplications", jobApplications);
        return "job-applications"; // JSP page name
    }

    @PostMapping("/delete-application")
    public String deleteApplication(@RequestParam("id") Integer id, Model model) {
        try {
            service.deleteApplication(id);
        } catch (EmptyResultDataAccessException e) {
            model.addAttribute("error", "Application with ID " + id + " does not exist.");
        }
        return "redirect:/applications"; // Redirect to the list of applications
    }

    
    @GetMapping("/verify-pin")
    public String showPinVerificationPage(Model model) {
        // Optional: Add model attributes if needed for the view
        return "verify-pin"; // JSP page for pin verification
    }

    @PostMapping("/verify-pin")
    public String verifyPin(@RequestParam("pin") String pin, Model model) {
        if (ADMIN_PIN.equals(pin)) {
            List<JobApplication> jobApplications = service.getAllApplications();
            model.addAttribute("jobApplications", jobApplications);
            return "job-applications"; // JSP page name for displaying job applications
        } else {
            model.addAttribute("error", "Invalid PIN. Please try again.");
            return "verify-pin"; // Return to pin verification page with error
        }
    }
}
