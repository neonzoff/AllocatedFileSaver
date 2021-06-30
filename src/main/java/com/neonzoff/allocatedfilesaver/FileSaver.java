package com.neonzoff.allocatedfilesaver;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author Tseplyaev Dmitry
 */
public class FileSaver implements Callable<Boolean> {
    private String fileName;
    private byte[] bytes;
    private CyclicBarrier cyclicBarrier;
//    private CountDownLatch countDownLatch;

    public FileSaver(String fileName, byte[] bytes, CyclicBarrier cyclicBarrier) {
        this.fileName = fileName;
        this.bytes = bytes;
        this.cyclicBarrier = cyclicBarrier;
//        this.countDownLatch = countDownLatch;
    }

    @Override
    public Boolean call() {
        try (BufferedOutputStream stream = new BufferedOutputStream(
                new FileOutputStream(
                        UUID.randomUUID() + fileName));) {
            stream.write(bytes);
            cyclicBarrier.await();
        } catch (IOException | BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
