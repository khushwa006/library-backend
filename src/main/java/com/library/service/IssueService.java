package com.library.service;

import com.library.entity.Book;
import com.library.entity.IssuedBook;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.IssuedBookRepository;
import com.library.repository.UserRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IssueService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final IssuedBookRepository issuedBookRepository;

    public IssueService(BookRepository bookRepository,
                        UserRepository userRepository,
                        IssuedBookRepository issuedBookRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.issuedBookRepository = issuedBookRepository;
    }

    // ================= ISSUE BOOK =================
    public IssuedBook issueBook(Long bookId, Long userId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is already issued!");
        }

        List<IssuedBook> activeIssues =
                issuedBookRepository.findByUserIdAndReturnedFalse(userId);

        if (activeIssues.size() >= 3) {
            throw new RuntimeException("User cannot issue more than 3 books!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        IssuedBook issuedBook = new IssuedBook();
        issuedBook.setBook(book);
        issuedBook.setUser(user);
        issuedBook.setIssueDate(LocalDate.now());
        issuedBook.setDueDate(LocalDate.now().plusDays(7));
        issuedBook.setReturned(false);
        issuedBook.setFineAmount(0);

        book.setAvailable(false);
        bookRepository.save(book);

        return issuedBookRepository.save(issuedBook);
    }

    // ================= RETURN BOOK =================
    public IssuedBook returnBook(Long issuedBookId) {

        IssuedBook issuedBook = issuedBookRepository.findById(issuedBookId)
                .orElseThrow(() -> new RuntimeException("Issued record not found"));

        issuedBook.setReturned(true);
        issuedBook.setReturnDate(LocalDate.now());

        if (LocalDate.now().isAfter(issuedBook.getDueDate())) {

            long daysLate = ChronoUnit.DAYS.between(
                    issuedBook.getDueDate(),
                    LocalDate.now()
            );

            issuedBook.setFineAmount(daysLate * 10);
        }

        Book book = issuedBook.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return issuedBookRepository.save(issuedBook);
    }

    // ================= GET USER ISSUED BOOKS =================
    public List<IssuedBook> getUserIssuedBooks(Long userId) {
        return issuedBookRepository.findByUserIdAndReturnedFalse(userId);
    }
}