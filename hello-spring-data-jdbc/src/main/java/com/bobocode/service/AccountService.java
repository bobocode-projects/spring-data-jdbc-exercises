package com.bobocode.service;

import com.bobocode.entity.Account;
import com.bobocode.exception.AccountNotFoundException;
import com.bobocode.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * {@link AccountService} is a simple service that provides some API for {@link Account} entity. It is using
 * {@link AccountRepository} in order to communicate with the database.
 */
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    /**
     * Stores account to the database and sets a generated id.
     *
     * @param account
     */
    public void saveAccount(Account account) {
        throw new UnsupportedOperationException("The task is NOT FINISHED"); // todo: implement according to the javadoc
    }

    /**
     * Loads {@link Account} by a given id. Throws {@link AccountNotFoundException} if Account with given id is not found.
     *
     * @param id account primary key
     * @return found account
     */
    public Account getAccountById(Long id) {
        throw new UnsupportedOperationException("The task is NOT FINISHED"); // todo: implement according to the javadoc
    }

    /**
     * Finds all saved accounts
     *
     * @return a list of all accounts
     */
    public List<Account> getAllAccounts() {
        throw new UnsupportedOperationException("The task is NOT FINISHED"); // todo: implement according to the javadoc
    }

    /**
     * Finds all accounts that has the given lastName. It uses a special method implemented by {@link AccountRepository}.
     * FILTERING BY LASTNAME SHOULD BE DONE ON the DATABASE SIDE.
     *
     * @param lastName to filter by
     * @return a list of accounts that has a given lastName
     */
    public List<Account> findAllByLastName(String lastName) {
        throw new UnsupportedOperationException("The task is NOT FINISHED"); // todo: implement according to the javadoc
    }

    /**
     * Updates a corresponding account record in the database using by its primary key (id).
     *
     * @param account that should be updated in the DB
     */
    public void updateAccount(Account account) {
        throw new UnsupportedOperationException("The task is not FINISHED"); // todo: implement according to the javadoc
    }

    /**
     * Removes a corresponding account record in the database by its primary key (id).
     *
     * @param id of an account that should be removed from the DB
     */
    public void removeAccountById(Long id) {
        throw new UnsupportedOperationException("The task is NOT FINISHED"); // todo: implement according to the javadoc
    }

    /**
     * An atomic operation that gives every account with a given lastName a bonus. In case or an error, no account
     * balances should be updated.
     *
     * @param bonusAmount a value that should be added to account balances
     * @param lastName    of accounts that should receive a bonus
     */
    public void bonusEveryoneByLastName(BigDecimal bonusAmount, String lastName) {
        throw new UnsupportedOperationException("The task is NOT FINISHED"); // todo: finish impl using bonusAccount()
    }

    private void bonusAccount(Account account, BigDecimal bonus) {
        account.setBalance(account.getBalance().add(bonus));
        updateAccount(account);
    }
}
