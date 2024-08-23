package ma.appsegov.authservice.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ImageService {
    public String saveImage(MultipartFile file) throws IOException;

    public Resource getProfile(String filename) throws MalformedURLException;

    public String getContentType(String fileName) throws IOException ;

    public void deleteProfile(String filename) throws IOException ;

    }
