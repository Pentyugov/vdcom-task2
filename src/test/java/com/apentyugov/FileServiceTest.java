package com.apentyugov;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    @Test
    void prepareOutputFile() {

        File file = FileService.prepareOutputFile();
        assertAll(
                () -> assertNotNull(file),
                () -> assertTrue(file.exists()),
                () -> assertTrue(file.isFile())
        );
    }
}