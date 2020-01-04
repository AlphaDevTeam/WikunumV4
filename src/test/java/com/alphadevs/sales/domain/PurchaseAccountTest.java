package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class PurchaseAccountTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseAccount.class);
        PurchaseAccount purchaseAccount1 = new PurchaseAccount();
        purchaseAccount1.setId(1L);
        PurchaseAccount purchaseAccount2 = new PurchaseAccount();
        purchaseAccount2.setId(purchaseAccount1.getId());
        assertThat(purchaseAccount1).isEqualTo(purchaseAccount2);
        purchaseAccount2.setId(2L);
        assertThat(purchaseAccount1).isNotEqualTo(purchaseAccount2);
        purchaseAccount1.setId(null);
        assertThat(purchaseAccount1).isNotEqualTo(purchaseAccount2);
    }
}
