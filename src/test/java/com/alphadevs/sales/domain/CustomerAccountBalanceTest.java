package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CustomerAccountBalanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAccountBalance.class);
        CustomerAccountBalance customerAccountBalance1 = new CustomerAccountBalance();
        customerAccountBalance1.setId(1L);
        CustomerAccountBalance customerAccountBalance2 = new CustomerAccountBalance();
        customerAccountBalance2.setId(customerAccountBalance1.getId());
        assertThat(customerAccountBalance1).isEqualTo(customerAccountBalance2);
        customerAccountBalance2.setId(2L);
        assertThat(customerAccountBalance1).isNotEqualTo(customerAccountBalance2);
        customerAccountBalance1.setId(null);
        assertThat(customerAccountBalance1).isNotEqualTo(customerAccountBalance2);
    }
}
