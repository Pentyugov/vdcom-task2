package com.apentyugov;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void start() throws IOException {
        int number = new Random().nextInt(10000);
        App.start(number);
        File file = App.getFile();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int value = Integer.parseInt(reader.readLine());
        reader.close();
        assertEquals(number, value);
    }
}
