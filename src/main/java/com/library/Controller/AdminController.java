package com.library.controller;

import com.library.entity.IssuedBook;
import com.library.repository.BookRepository;
import com.library.repository.IssuedBookRepository;
import com.library.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final IssuedBookRepository issuedBookRepository;

    public AdminController(BookRepository bookRepository,
                           UserRepository userRepository,
                           IssuedBookRepository issuedBookRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.issuedBookRepository = issuedBookRepository;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {

        Map<String, Object> data = new HashMap<>();

        data.put("totalBooks", bookRepository.count());
        data.put("totalUsers", userRepository.count());

        List<IssuedBook> active =
                issuedBookRepository.findAll()
                        .stream()
                        .filter(b -> !b.isReturned())
                        .toList();

        data.put("activeIssues", active.size());

        double totalFine = issuedBookRepository.findAll()
                .stream()
                .mapToDouble(IssuedBook::getFineAmount)
                .sum();

        data.put("totalFineCollected", totalFine);

        return data;
    }
}