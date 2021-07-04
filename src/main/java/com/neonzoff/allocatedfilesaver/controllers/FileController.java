package com.neonzoff.allocatedfilesaver.controllers;

import com.neonzoff.allocatedfilesaver.FileSaver;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.concurrent.*;

/**
 * @author Tseplyaev Dmitry
 */
@RestController
@RequestMapping("file")
public class FileController {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    private CountDownLatch countDownLatch = new CountDownLatch(2);


    @PostMapping()
    public @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                executorService.submit(new FileSaver(file.getOriginalFilename(), file.getBytes(), countDownLatch));
                executorService.submit(new FileSaver(file.getOriginalFilename(), file.getBytes(), countDownLatch));
                executorService.submit(new FileSaver(file.getOriginalFilename(), file.getBytes(), countDownLatch));
                try {
                    countDownLatch.await();
                    return "SUCCESS";
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        }
        return "File is empty";
    }

}
