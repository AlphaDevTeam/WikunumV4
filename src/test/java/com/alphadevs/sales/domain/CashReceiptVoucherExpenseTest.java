package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CashReceiptVoucherExpenseTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashReceiptVoucherExpense.class);
        CashReceiptVoucherExpense cashReceiptVoucherExpense1 = new CashReceiptVoucherExpense();
        cashReceiptVoucherExpense1.setId(1L);
        CashReceiptVoucherExpense cashReceiptVoucherExpense2 = new CashReceiptVoucherExpense();
        cashReceiptVoucherExpense2.setId(cashReceiptVoucherExpense1.getId());
        assertThat(cashReceiptVoucherExpense1).isEqualTo(cashReceiptVoucherExpense2);
        cashReceiptVoucherExpense2.setId(2L);
        assertThat(cashReceiptVoucherExpense1).isNotEqualTo(cashReceiptVoucherExpense2);
        cashReceiptVoucherExpense1.setId(null);
        assertThat(cashReceiptVoucherExpense1).isNotEqualTo(cashReceiptVoucherExpense2);
    }
}
