package com.finwing.repository;

import com.finwing.entity.User;
import com.finwing.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserUserId(Long userId);

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserOrderByCreatedAtDesc(User user);

    // ✅ FIXED (date → createdAt)
    List<Transaction> findByUserAndCreatedAtBetweenOrderByCreatedAtDesc(
            User user,
            LocalDateTime from,
            LocalDateTime to
    );

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = :type")
    Double sumAmountByUserAndType(@Param("user") User user, @Param("type") String type);

    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = 'expense' GROUP BY t.category")
    List<Object[]> sumExpensesByCategory(@Param("user") User user);

    // ✅ FIXED
    List<Transaction> findAllByOrderByCreatedAtDesc();
    void deleteByUser(User user); 
    

}
