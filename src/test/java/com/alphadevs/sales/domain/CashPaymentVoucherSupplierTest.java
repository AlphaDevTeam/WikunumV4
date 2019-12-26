package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CashPaymentVoucherSupplierTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashPaymentVoucherSupplier.class);
        CashPaymentVoucherSupplier cashPaymentVoucherSupplier1 = new CashPaymentVoucherSupplier();
        cashPaymentVoucherSupplier1.setId(1L);
        CashPaymentVoucherSupplier cashPaymentVoucherSupplier2 = new CashPaymentVoucherSupplier();
        cashPaymentVoucherSupplier2.setId(cashPaymentVoucherSupplier1.getId());
        assertThat(cashPaymentVoucherSupplier1).isEqualTo(cashPaymentVoucherSupplier2);
        cashPaymentVoucherSupplier2.setId(2L);
        assertThat(cashPaymentVoucherSupplier1).isNotEqualTo(cashPaymentVoucherSupplier2);
        cashPaymentVoucherSupplier1.setId(null);
        assertThat(cashPaymentVoucherSupplier1).isNotEqualTo(cashPaymentVoucherSupplier2);
    }
}
