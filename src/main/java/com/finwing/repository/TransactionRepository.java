package com.finwing.repository;

import com.finwing.entity.User;
import com.finwing.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);
}
