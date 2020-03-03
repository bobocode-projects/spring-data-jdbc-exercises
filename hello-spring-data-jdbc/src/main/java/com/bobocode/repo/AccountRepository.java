package com.bobocode.repo;

import com.bobocode.entity.Account;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {@link AccountRepository} represents a Data Access Object for an {@link Account} entity that is build using
 * Spring Data JDBC.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Override
    Optional<Account> findById(Long id);

    @Override
    List<Account> findAll();

    @Query("SELECT * FROM accounts WHERE accounts.last_Name = :lastName")
    List<Account> findAccountsByLastName(@Param("lastName") String lastName);
}



