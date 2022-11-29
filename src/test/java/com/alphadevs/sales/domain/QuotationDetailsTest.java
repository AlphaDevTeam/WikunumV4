package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class QuotationDetailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationDetails.class);
        QuotationDetails quotationDetails1 = new QuotationDetails();
        quotationDetails1.setId(1L);
        QuotationDetails quotationDetails2 = new QuotationDetails();
        quotationDetails2.setId(quotationDetails1.getId());
        assertThat(quotationDetails1).isEqualTo(quotationDetails2);
        quotationDetails2.setId(2L);
        assertThat(quotationDetails1).isNotEqualTo(quotationDetails2);
        quotationDetails1.setId(null);
        assertThat(quotationDetails1).isNotEqualTo(quotationDetails2);
    }
}
