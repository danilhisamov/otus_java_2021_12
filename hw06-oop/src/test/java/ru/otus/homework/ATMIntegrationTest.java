package ru.otus.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static ru.otus.homework.Nominal.*;

class ATMIntegrationTest {

    private ATM atm;

    @BeforeEach
    void setUp() {
        atm = new ATM();
    }

    @Test
    void receive() {
        var moneyMap = Map.of(
                NOMINAL_50, 1,
                NOMINAL_100, 1,
                NOMINAL_500, 1,
                NOMINAL_1000, 1,
                NOMINAL_5000, 1
        );
        atm.receive(moneyMap);
        assertThat(atm.getBalanceAsMap()).isEqualTo(moneyMap);
    }

    @Test
    void receiveCollectionOfBanknotes() {
        var expected = Map.of(
                NOMINAL_50, 2,
                NOMINAL_100, 1,
                NOMINAL_500, 2
        );
        atm.receive(List.of(NOMINAL_50, NOMINAL_50, NOMINAL_100, NOMINAL_500, NOMINAL_500));
        assertThat(atm.getBalanceAsMap()).isEqualTo(expected);
    }

    @Test
    void give() {
        var moneyMap = Map.of(
                NOMINAL_50, 1,
                NOMINAL_100, 1,
                NOMINAL_500, 1,
                NOMINAL_1000, 1,
                NOMINAL_5000, 1
        );
        atm.receive(moneyMap);

        var expectedMoney = Map.of(
                NOMINAL_50, 1,
                NOMINAL_500, 1
        );
        assertThat(atm.give(550)).isEqualTo(expectedMoney);

        var expectedHolder = Map.of(
                NOMINAL_50, 0,
                NOMINAL_100, 1,
                NOMINAL_500, 0,
                NOMINAL_1000, 1,
                NOMINAL_5000, 1
        );
        assertThat(atm.getBalanceAsMap()).isEqualTo(expectedHolder);
    }

    @Test
    void giveRequestTooMuchMoney() {
        var moneyMap = Map.of(
                NOMINAL_50, 1,
                NOMINAL_100, 1,
                NOMINAL_500, 1,
                NOMINAL_1000, 1,
                NOMINAL_5000, 1
        );
        atm.receive(moneyMap);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> atm.give(8000));
    }

    @Test
    void giveWithMinimalAmountOfBanknotes() {
        var moneyMap = Map.of(
                NOMINAL_50, 6,
                NOMINAL_100, 5,
                NOMINAL_500, 4,
                NOMINAL_1000, 2,
                NOMINAL_5000, 1
        );
        atm.receive(moneyMap);

        atm.give(6650);

        var expected = Map.of(
                NOMINAL_50, 5,
                NOMINAL_100, 4,
                NOMINAL_500, 3,
                NOMINAL_1000, 1,
                NOMINAL_5000, 0
        );
        assertThat(atm.getBalanceAsMap()).isEqualTo(expected);
    }

    @Test
    void balanceAsStringWithEmptyATM() {
        assertThat(atm.getBalanceAsString()).isEqualTo("{}");
    }

    @Test
    void balanceAsString() {
        var moneyMap = Map.of(
                NOMINAL_50, 1,
                NOMINAL_100, 1,
                NOMINAL_500, 1,
                NOMINAL_1000, 1,
                NOMINAL_5000, 1
        );
        atm.receive(moneyMap);
        assertThat(atm.getBalanceAsString()).isNotBlank();
    }
}
