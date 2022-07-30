package com.RestAPI.v1.Server.IService;

import org.springframework.web.multipart.MultipartFile;

public interface IuploadService {
    String uploadFile(String file);


    String dummyFileUpload(String file);
}
