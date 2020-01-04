package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CashPaymentVoucherExpenseTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashPaymentVoucherExpense.class);
        CashPaymentVoucherExpense cashPaymentVoucherExpense1 = new CashPaymentVoucherExpense();
        cashPaymentVoucherExpense1.setId(1L);
        CashPaymentVoucherExpense cashPaymentVoucherExpense2 = new CashPaymentVoucherExpense();
        cashPaymentVoucherExpense2.setId(cashPaymentVoucherExpense1.getId());
        assertThat(cashPaymentVoucherExpense1).isEqualTo(cashPaymentVoucherExpense2);
        cashPaymentVoucherExpense2.setId(2L);
        assertThat(cashPaymentVoucherExpense1).isNotEqualTo(cashPaymentVoucherExpense2);
        cashPaymentVoucherExpense1.setId(null);
        assertThat(cashPaymentVoucherExpense1).isNotEqualTo(cashPaymentVoucherExpense2);
    }
}
