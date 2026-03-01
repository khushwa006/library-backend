package com.library;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemMonitor {

    @Scheduled(fixedRate = 60000)
    public void generateReport() {

        System.out.println("Scheduled task running...");

        try (FileWriter writer = new FileWriter("report.txt", true)) {
            writer.write("Report generated at: " + LocalDateTime.now() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}