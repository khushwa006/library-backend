package com.library.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    @Async("mlExecutor")
    public void generateRecommendation(Long bookId) {
        System.out.println("Running in thread: " + Thread.currentThread().getName());

        try {
            Thread.sleep(3000); // simulate ML processing time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Recommendation generated for book id: " + bookId);
    }
}