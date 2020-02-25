package com.bobocode.repo;

import com.bobocode.entity.Account;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * {@link AccountRepository} represents a Data Access Object for an {@link Account} entity that is build using
 * Spring Data JDBC.
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("select * from accounts a where a.last_name = :lastName")
    List<Account> findAllByLastName(@Param("lastName") String lastName);

}
