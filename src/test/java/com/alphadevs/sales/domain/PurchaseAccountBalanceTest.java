package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class PurchaseAccountBalanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseAccountBalance.class);
        PurchaseAccountBalance purchaseAccountBalance1 = new PurchaseAccountBalance();
        purchaseAccountBalance1.setId(1L);
        PurchaseAccountBalance purchaseAccountBalance2 = new PurchaseAccountBalance();
        purchaseAccountBalance2.setId(purchaseAccountBalance1.getId());
        assertThat(purchaseAccountBalance1).isEqualTo(purchaseAccountBalance2);
        purchaseAccountBalance2.setId(2L);
        assertThat(purchaseAccountBalance1).isNotEqualTo(purchaseAccountBalance2);
        purchaseAccountBalance1.setId(null);
        assertThat(purchaseAccountBalance1).isNotEqualTo(purchaseAccountBalance2);
    }
}
