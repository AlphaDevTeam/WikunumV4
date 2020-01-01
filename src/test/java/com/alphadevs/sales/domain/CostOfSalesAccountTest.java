package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class CostOfSalesAccountTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CostOfSalesAccount.class);
        CostOfSalesAccount costOfSalesAccount1 = new CostOfSalesAccount();
        costOfSalesAccount1.setId(1L);
        CostOfSalesAccount costOfSalesAccount2 = new CostOfSalesAccount();
        costOfSalesAccount2.setId(costOfSalesAccount1.getId());
        assertThat(costOfSalesAccount1).isEqualTo(costOfSalesAccount2);
        costOfSalesAccount2.setId(2L);
        assertThat(costOfSalesAccount1).isNotEqualTo(costOfSalesAccount2);
        costOfSalesAccount1.setId(null);
        assertThat(costOfSalesAccount1).isNotEqualTo(costOfSalesAccount2);
    }
}
