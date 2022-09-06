package com.apentyugov;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class App {

    private static File file;
    private static final Object lock = new Object();
    private static volatile int COUNT = 1;
    private static final ReentrantLock locker = new ReentrantLock();


    public static void main(String[] args) {
        System.out.println("Enter a number");
        Scanner scanner = new Scanner(System.in);
        int finishResult = Integer.parseInt(scanner.nextLine());
        start(finishResult);
    }

    public static void start(Integer number) {
        file = FileService.prepareOutputFile();
        methodOne(number);
//        methodTwo(number);
    }


    private static void methodOne(int finishResult) {

        Thread thread1 = new Thread(() -> fillFile(finishResult));
        Thread thread2 = new Thread(() -> fillFile(finishResult));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int value = Integer.parseInt(reader.readLine());
            System.out.println("Final result: " + value);
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static void fillFile(int finishResult) {
        while (COUNT < finishResult) {
            synchronized (lock) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    int oldValue = Integer.parseInt(reader.readLine());
                    System.out.println("Old value: " + oldValue + "; New value: " + COUNT + "; Thread: " + Thread.currentThread().getName());
                    reader.close();
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(String.valueOf(COUNT));
                    fileWriter.close();
                    COUNT += 1;
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    // Not works correctly
    private static void methodTwo(int finishResult) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        while (COUNT <= finishResult) {
            pool.submit(new Work(COUNT, file, locker));
            COUNT += 1;
        }
        pool.shutdown();
    }

    public static File getFile() {
        return file;
    }

}

class Work implements Runnable {

    private final int newValue;
    private final File file;
    private final ReentrantLock locker;

    public Work(int newValue, File file, ReentrantLock locker) {
        this.newValue = newValue;
        this.file = file;
        this.locker = locker;
    }

    @Override
    public void run() {
        try {
            locker.lock();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            int oldValue = Integer.parseInt(reader.readLine());
            System.out.println("Old value: " + oldValue + "; New value: " + newValue + "; Thread: " + Thread.currentThread().getName());
            reader.close();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(String.valueOf(newValue));
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

}
