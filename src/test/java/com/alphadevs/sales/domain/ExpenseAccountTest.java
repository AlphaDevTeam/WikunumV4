package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class ExpenseAccountTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseAccount.class);
        ExpenseAccount expenseAccount1 = new ExpenseAccount();
        expenseAccount1.setId(1L);
        ExpenseAccount expenseAccount2 = new ExpenseAccount();
        expenseAccount2.setId(expenseAccount1.getId());
        assertThat(expenseAccount1).isEqualTo(expenseAccount2);
        expenseAccount2.setId(2L);
        assertThat(expenseAccount1).isNotEqualTo(expenseAccount2);
        expenseAccount1.setId(null);
        assertThat(expenseAccount1).isNotEqualTo(expenseAccount2);
    }
}
