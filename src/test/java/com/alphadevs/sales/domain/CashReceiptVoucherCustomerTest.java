package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CashReceiptVoucherCustomerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashReceiptVoucherCustomer.class);
        CashReceiptVoucherCustomer cashReceiptVoucherCustomer1 = new CashReceiptVoucherCustomer();
        cashReceiptVoucherCustomer1.setId(1L);
        CashReceiptVoucherCustomer cashReceiptVoucherCustomer2 = new CashReceiptVoucherCustomer();
        cashReceiptVoucherCustomer2.setId(cashReceiptVoucherCustomer1.getId());
        assertThat(cashReceiptVoucherCustomer1).isEqualTo(cashReceiptVoucherCustomer2);
        cashReceiptVoucherCustomer2.setId(2L);
        assertThat(cashReceiptVoucherCustomer1).isNotEqualTo(cashReceiptVoucherCustomer2);
        cashReceiptVoucherCustomer1.setId(null);
        assertThat(cashReceiptVoucherCustomer1).isNotEqualTo(cashReceiptVoucherCustomer2);
    }
}
