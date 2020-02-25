package com.bobocode.service;

import com.bobocode.entity.Account;
import com.bobocode.exception.AccountNotFoundException;
import com.bobocode.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        accountRepository.save(account);
    }

    /**
     * Loads {@link Account} by a given id. Throws {@link AccountNotFoundException} if Account with given id is not found.
     *
     * @param id account primary key
     * @return found account
     */
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %d does not exist", id)));
    }

    /**
     * Finds all saved accounts
     *
     * @return a list of all accounts
     */
    public List<Account> getAllAccounts() {
        Iterable<Account> accountIterable = accountRepository.findAll();
        List<Account> accountList = new ArrayList<>();
        accountIterable.forEach(accountList::add);
        return accountList;
    }

    /**
     * Finds all accounts that has the given lastName. It uses a special method implemented by {@link AccountRepository}.
     * FILTERING BY LASTNAME SHOULD BE DONE ON the DATABASE SIDE.
     *
     * @param lastName to filter by
     * @return a list of accounts that has a given lastName
     */
    public List<Account> findAllByLastName(String lastName) {
        return accountRepository.findAllByLastName(lastName);
    }

    /**
     * Updates a corresponding account record in the database using by its primary key (id).
     *
     * @param account that should be updated in the DB
     */
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    /**
     * Removes a corresponding account record in the database by its primary key (id).
     *
     * @param id of an account that should be removed from the DB
     */
    public void removeAccountById(Long id) {
        accountRepository.deleteById(id);
    }

    /**
     * An atomic operation that gives every account with a given lastName a bonus. In case or an error, no account
     * balances should be updated.
     *
     * @param bonusAmount a value that should be added to account balances
     * @param lastName    of accounts that should receive a bonus
     */
    @Transactional
    public void bonusEveryoneByLastName(BigDecimal bonusAmount, String lastName) {
        List<Account> accountList = findAllByLastName(lastName);
        accountList.forEach(account -> bonusAccount(account, bonusAmount));
    }

    private void bonusAccount(Account account, BigDecimal bonus) {
        account.setBalance(account.getBalance().add(bonus));
        updateAccount(account);
    }
}
