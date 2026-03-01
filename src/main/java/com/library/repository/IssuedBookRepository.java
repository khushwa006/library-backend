package com.library.repository;

import com.library.entity.IssuedBook;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedBookRepository extends JpaRepository<IssuedBook, Long> {

    List<IssuedBook> findByUserIdAndReturnedFalse(Long userId);

    Optional<IssuedBook> findByBookIdAndReturnedFalse(Long bookId);
}