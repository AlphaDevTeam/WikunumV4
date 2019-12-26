package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class PaymentTypesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentTypes.class);
        PaymentTypes paymentTypes1 = new PaymentTypes();
        paymentTypes1.setId(1L);
        PaymentTypes paymentTypes2 = new PaymentTypes();
        paymentTypes2.setId(paymentTypes1.getId());
        assertThat(paymentTypes1).isEqualTo(paymentTypes2);
        paymentTypes2.setId(2L);
        assertThat(paymentTypes1).isNotEqualTo(paymentTypes2);
        paymentTypes1.setId(null);
        assertThat(paymentTypes1).isNotEqualTo(paymentTypes2);
    }
}
