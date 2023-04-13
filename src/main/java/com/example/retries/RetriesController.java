package com.example.retries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/retry")
public class RetriesController {

    @Autowired
    DownloaderService downloaderService;

    @GetMapping(value = "/simplebackoff")
    public void downloadWithSimpleBackoff(){
        downloaderService.downloadWithSimpleBackoff();
    }

    @GetMapping(value = "/expbackoff")
    public void downloadExponentialBackoff(){
        downloaderService.downloadExponentialBackoff();
    }

    @GetMapping(value = "/randombackoff")
    public void downloadRandomBackoff(){
        downloaderService.downloadRandomBackoff();
    }

    @GetMapping(value = "/retrytemplate")
    public void downloadRetryTemplate(){
        downloaderService.downloadRetryTemplate();
    }

}
