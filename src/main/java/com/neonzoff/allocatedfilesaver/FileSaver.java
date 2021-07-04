package com.neonzoff.allocatedfilesaver;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author Tseplyaev Dmitry
 */
public class FileSaver implements Runnable {
    private String fileName;
    private byte[] bytes;
    private CountDownLatch countDownLatch;

    public FileSaver(String fileName, byte[] bytes, CountDownLatch countDownLatch) {
        this.fileName = fileName;
        this.bytes = bytes;
//        this.cyclicBarrier = cyclicBarrier;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try (BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(
                        UUID.randomUUID() + fileName));) {
            stream.write(bytes);
//            System.out.println("saved" + countDownLatch.getCount());
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
