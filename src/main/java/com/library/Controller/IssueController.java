package com.library.controller;

import com.library.entity.IssuedBook;
import com.library.service.IssueService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping("/{bookId}/user/{userId}")
    public IssuedBook issueBook(@PathVariable Long bookId, @PathVariable Long userId) {
        return issueService.issueBook(bookId, userId);
    }

    @PostMapping("/return/{issuedId}")
    public IssuedBook returnBook(@PathVariable Long issuedId) {
        return issueService.returnBook(issuedId);
    }

    @GetMapping("/user/{userId}")
    public List<IssuedBook> getUserBooks(@PathVariable Long userId) {
        return issueService.getUserIssuedBooks(userId);
    }
}