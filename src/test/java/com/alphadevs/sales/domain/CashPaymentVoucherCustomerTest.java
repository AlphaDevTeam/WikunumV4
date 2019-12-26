package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CashPaymentVoucherCustomerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashPaymentVoucherCustomer.class);
        CashPaymentVoucherCustomer cashPaymentVoucherCustomer1 = new CashPaymentVoucherCustomer();
        cashPaymentVoucherCustomer1.setId(1L);
        CashPaymentVoucherCustomer cashPaymentVoucherCustomer2 = new CashPaymentVoucherCustomer();
        cashPaymentVoucherCustomer2.setId(cashPaymentVoucherCustomer1.getId());
        assertThat(cashPaymentVoucherCustomer1).isEqualTo(cashPaymentVoucherCustomer2);
        cashPaymentVoucherCustomer2.setId(2L);
        assertThat(cashPaymentVoucherCustomer1).isNotEqualTo(cashPaymentVoucherCustomer2);
        cashPaymentVoucherCustomer1.setId(null);
        assertThat(cashPaymentVoucherCustomer1).isNotEqualTo(cashPaymentVoucherCustomer2);
    }
}
