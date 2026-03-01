package com.library.scheduler;

import com.library.entity.IssuedBook;
import com.library.repository.IssuedBookRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FineScheduler {

    private final IssuedBookRepository issuedBookRepository;

    public FineScheduler(IssuedBookRepository issuedBookRepository) {
        this.issuedBookRepository = issuedBookRepository;
    }

    // Runs daily at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateFines() {

        List<IssuedBook> issuedBooks = issuedBookRepository.findAll();

        for (IssuedBook book : issuedBooks) {

            if (!book.isReturned() &&
                LocalDate.now().isAfter(book.getDueDate())) {

                long daysLate = ChronoUnit.DAYS.between(
                        book.getDueDate(),
                        LocalDate.now()
                );

                book.setFineAmount(daysLate * 10);
                issuedBookRepository.save(book);
            }
        }
    }
}