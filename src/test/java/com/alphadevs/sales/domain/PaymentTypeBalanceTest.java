package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class PaymentTypeBalanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentTypeBalance.class);
        PaymentTypeBalance paymentTypeBalance1 = new PaymentTypeBalance();
        paymentTypeBalance1.setId(1L);
        PaymentTypeBalance paymentTypeBalance2 = new PaymentTypeBalance();
        paymentTypeBalance2.setId(paymentTypeBalance1.getId());
        assertThat(paymentTypeBalance1).isEqualTo(paymentTypeBalance2);
        paymentTypeBalance2.setId(2L);
        assertThat(paymentTypeBalance1).isNotEqualTo(paymentTypeBalance2);
        paymentTypeBalance1.setId(null);
        assertThat(paymentTypeBalance1).isNotEqualTo(paymentTypeBalance2);
    }
}
