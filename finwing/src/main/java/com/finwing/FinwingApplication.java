package com.finwing;

<<<<<<< HEAD
=======

>>>>>>> 270937aaa9b76eb90922b24ff83670efbd0557d4
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinwingApplication {

    static {
        // Ensure PostgreSQL gets a valid timezone in all boot paths (app + tests).
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

        SpringApplication.run(FinwingApplication.class, args);
    }
}