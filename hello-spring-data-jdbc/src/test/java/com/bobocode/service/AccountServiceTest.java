package com.bobocode.service;

import com.bobocode.entity.Account;
import com.bobocode.exception.AccountNotFoundException;
import com.bobocode.util.TestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @AfterEach
    public void clear() {
        jdbcTemplate.execute("truncate table accounts");
    }

    @Test
    public void saveAccount() {
        Account account = TestDataGenerator.generateAccount();

        accountService.saveAccount(account);

        assertThat(account.getId()).isNotNull();
    }

    @Test
    public void getAccountById() {
        Account account = saveTestAccount();

        Account foundAccount = accountService.getAccountById(account.getId());

        assertThat(foundAccount).isEqualTo(account);
    }

    @Test
    public void getAccountByIdThrowsExceptionWhenAccountNotFound() {
        Long fakeId = 666L;

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(fakeId));
    }

    @Test
    public void getAllAccounts() {
        List<Account> initialAccountList = TestDataGenerator.generateAccountList(10);
        initialAccountList.forEach(this::saveAccount);

        List<Account> foundAccounts = accountService.getAllAccounts();

        assertThat(foundAccounts).isEqualTo(initialAccountList);
    }

    @Test
    public void updateAccount() {
        Account account = saveTestAccount();

        account.setBalance(account.getBalance().add(BigDecimal.TEN));
        accountService.updateAccount(account);
        BigDecimal foundBalance = jdbcTemplate.queryForObject("select balance from accounts where id = ?",
                BigDecimal.class, account.getId());

        assertThat(foundBalance).isEqualTo(account.getBalance());
    }

    @Test
    public void removeAccountById() {
        Account account = saveTestAccount();

        accountService.removeAccountById(account.getId());
        Boolean isRemoved = jdbcTemplate.queryForObject("select count(*) = 0 from accounts where id = ?",
                Boolean.class, account.getId());

        assertTrue(isRemoved);
    }

    @Test
    public void findAllByLastName() {
        String testLastName = "Muriel";
        List<Account> testAccounts = TestDataGenerator.generateAccountList(2);
        testAccounts.forEach(a -> {
            a.setLastName(testLastName);
            saveAccount(a);
        });
        TestDataGenerator.generateAccountList(3).forEach(this::saveAccount);

        List<Account> foundAccounts = accountService.findAllByLastName(testLastName);

        assertThat(foundAccounts).containsExactlyInAnyOrder(testAccounts.toArray(new Account[]{}));
    }

    @Test
    public void bonusEveryoneByLastName() {
        List<Account> targetAccounts = TestDataGenerator.generateAccountList(3);
        String targetLastName = "Simpson";
        targetAccounts.forEach(account -> {
            account.setLastName(targetLastName);
            saveAccount(account);
        });

        BigDecimal bonusAmount = BigDecimal.valueOf(1500);
        List<BigDecimal> expectedBalances = targetAccounts.stream()
                .map(account -> account.getBalance().add(bonusAmount))
                .collect(Collectors.toList());
        accountService.bonusEveryoneByLastName(bonusAmount, targetLastName);
        List<BigDecimal> foundBalances = jdbcTemplate.queryForList("select balance from accounts where last_name = ?",
                BigDecimal.class, targetLastName);

        assertThat(foundBalances).isEqualTo(expectedBalances);
    }

    @Test
    public void bonusEveryoneByLastNameDoesNotUpdateOtherAccounts() {
        List<Account> accountList = TestDataGenerator.generateAccountList(5);
        List<BigDecimal> initialBalances = accountList.stream().map(Account::getBalance).collect(Collectors.toList());
        accountList.forEach(this::saveAccount);

        accountService.bonusEveryoneByLastName(BigDecimal.valueOf(666), "XXXXX");
        List<BigDecimal> foundBalances = jdbcTemplate.queryForList("select balance from accounts", BigDecimal.class);

        assertThat(foundBalances).isEqualTo(initialBalances);
    }

    @Test
    @Rollback(false)
    public void bonusEveryoneByLastNameIsAtomic() {
        List<Account> accountList = TestDataGenerator.generateAccountList(5);
        accountList.get(3).setBalance(BigDecimal.ONE.setScale(2));
        List<BigDecimal> initialBalances = accountList.stream().map(Account::getBalance).collect(Collectors.toList());
        String targetLastName = "Bobby";
        accountList.forEach(account -> {
            account.setLastName(targetLastName);
            saveAccount(account);
        });

        jdbcTemplate.execute("alter table accounts add constraint positive_balance CHECK ( balance > 0 );");
        try {
            accountService.bonusEveryoneByLastName(BigDecimal.valueOf(-10), targetLastName);
        } catch (Exception e) {
        }
        List<BigDecimal> foundBalances = jdbcTemplate.queryForList("select balance from accounts", BigDecimal.class);

        assertThat(foundBalances).isEqualTo(initialBalances);
    }

    private Account saveTestAccount() {
        Account account = TestDataGenerator.generateAccount();
        saveAccount(account);
        return account;
    }

    private void saveAccount(Account account) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> prepareInsertStatement(connection, account), keyHolder);
        Long id = (Long) keyHolder.getKey();
        account.setId(id);
    }

    private PreparedStatement prepareInsertStatement(Connection connection, Account account) throws SQLException {
        final String sql = "insert into accounts(first_name, last_name, email, birthday, creation_time, balance) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, account.getFirstName());
        ps.setString(2, account.getLastName());
        ps.setString(3, account.getEmail());
        ps.setDate(4, Date.valueOf(account.getBirthday()));
        ps.setTimestamp(5, Timestamp.valueOf(account.getCreationTime()));
        ps.setBigDecimal(6, account.getBalance());
        return ps;
    }
}
