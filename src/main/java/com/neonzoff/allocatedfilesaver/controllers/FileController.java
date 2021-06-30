package com.neonzoff.allocatedfilesaver.controllers;

import com.neonzoff.allocatedfilesaver.FileSaver;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.*;
import java.util.concurrent.*;

/**
 * @author Tseplyaev Dmitry
 */
@RestController
@RequestMapping("file")
@MultipartConfig(maxFileSize = 1024)
public class FileController {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);
    private Future<Boolean> future1;
    private Future<Boolean> future2;
    private Future<Boolean> future3;
    //    private CountDownLatch countDownLatch = new CountDownLatch(2);
//    private CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new SuccessMessage());
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(3);


    @PostMapping()
    public @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                future1 = executorService.submit(new FileSaver(file.getOriginalFilename(), file.getBytes(), cyclicBarrier));
                future2 = executorService.submit(new FileSaver(file.getOriginalFilename(), file.getBytes(), cyclicBarrier));
                future3 = executorService.submit(new FileSaver(file.getOriginalFilename(), file.getBytes(), cyclicBarrier));
                if (future1.get() && future2.get() ||
                        future1.get() && future3.get() ||
                        future2.get() && future3.get()) {
                    return "SUCCESS";
                }
            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        }
        return "File is empty";
    }

}
