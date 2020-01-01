package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CostOfSalesAccountBalanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostOfSalesAccountBalance.class);
        CostOfSalesAccountBalance costOfSalesAccountBalance1 = new CostOfSalesAccountBalance();
        costOfSalesAccountBalance1.setId(1L);
        CostOfSalesAccountBalance costOfSalesAccountBalance2 = new CostOfSalesAccountBalance();
        costOfSalesAccountBalance2.setId(costOfSalesAccountBalance1.getId());
        assertThat(costOfSalesAccountBalance1).isEqualTo(costOfSalesAccountBalance2);
        costOfSalesAccountBalance2.setId(2L);
        assertThat(costOfSalesAccountBalance1).isNotEqualTo(costOfSalesAccountBalance2);
        costOfSalesAccountBalance1.setId(null);
        assertThat(costOfSalesAccountBalance1).isNotEqualTo(costOfSalesAccountBalance2);
    }
}
