package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class PaymentTypeAccountTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentTypeAccount.class);
        PaymentTypeAccount paymentTypeAccount1 = new PaymentTypeAccount();
        paymentTypeAccount1.setId(1L);
        PaymentTypeAccount paymentTypeAccount2 = new PaymentTypeAccount();
        paymentTypeAccount2.setId(paymentTypeAccount1.getId());
        assertThat(paymentTypeAccount1).isEqualTo(paymentTypeAccount2);
        paymentTypeAccount2.setId(2L);
        assertThat(paymentTypeAccount1).isNotEqualTo(paymentTypeAccount2);
        paymentTypeAccount1.setId(null);
        assertThat(paymentTypeAccount1).isNotEqualTo(paymentTypeAccount2);
    }
}
