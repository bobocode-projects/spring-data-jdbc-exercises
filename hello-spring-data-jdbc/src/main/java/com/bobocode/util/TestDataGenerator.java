package com.bobocode.util;

import com.bobocode.entity.Account;
import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * A util tool that helps to build nice test data. It is based on JFairy library.
 */
public class TestDataGenerator {
    public static final int MIN_BALANCE_VALUE = 1000;
    public static final int MAX_BALANCE_VALUE = 200_000;

    public static Account generateAccount() {
        Person person = generatePerson();
        Account account = convertToAccount(person);
        fillCommonRandomFields(account);
        return account;
    }

    public static List<Account> generateAccountList(int size) {
        return Stream.generate(TestDataGenerator::generateAccount)
                .limit(size)
                .collect(toList());
    }

    private static Person generatePerson() {
        Fairy fairy = Fairy.create();
        return fairy.person();
    }

    private static Account convertToAccount(Person person) {
        Account account = new Account();
        fillAccount(account, person);
        return account;
    }

    private static void fillAccount(Account account, Person person) {
        account.setFirstName(person.getFirstName());
        account.setLastName(person.getLastName());
        account.setEmail(person.getEmail());
        account.setBirthday(LocalDate.of(
                person.getDateOfBirth().getYear(),
                person.getDateOfBirth().getMonthOfYear(),
                person.getDateOfBirth().getDayOfMonth()));
    }

    private static void fillCommonRandomFields(Account account) {
        BigDecimal balance = randomBigDecimal(MIN_BALANCE_VALUE, MAX_BALANCE_VALUE);
        account.setBalance(balance);
        account.setCreationTime(LocalDateTime.now());
    }

    private static BigDecimal randomBigDecimal(int min, int max) {
        int randomInt = ThreadLocalRandom.current().nextInt(min, max);
        return BigDecimal.valueOf(randomInt).setScale(2);
    }
}

