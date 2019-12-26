package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CashReceiptVoucherSupplierTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CashReceiptVoucherSupplier.class);
        CashReceiptVoucherSupplier cashReceiptVoucherSupplier1 = new CashReceiptVoucherSupplier();
        cashReceiptVoucherSupplier1.setId(1L);
        CashReceiptVoucherSupplier cashReceiptVoucherSupplier2 = new CashReceiptVoucherSupplier();
        cashReceiptVoucherSupplier2.setId(cashReceiptVoucherSupplier1.getId());
        assertThat(cashReceiptVoucherSupplier1).isEqualTo(cashReceiptVoucherSupplier2);
        cashReceiptVoucherSupplier2.setId(2L);
        assertThat(cashReceiptVoucherSupplier1).isNotEqualTo(cashReceiptVoucherSupplier2);
        cashReceiptVoucherSupplier1.setId(null);
        assertThat(cashReceiptVoucherSupplier1).isNotEqualTo(cashReceiptVoucherSupplier2);
    }
}
