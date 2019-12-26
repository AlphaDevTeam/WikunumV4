package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class InvoiceDetailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDetails.class);
        InvoiceDetails invoiceDetails1 = new InvoiceDetails();
        invoiceDetails1.setId(1L);
        InvoiceDetails invoiceDetails2 = new InvoiceDetails();
        invoiceDetails2.setId(invoiceDetails1.getId());
        assertThat(invoiceDetails1).isEqualTo(invoiceDetails2);
        invoiceDetails2.setId(2L);
        assertThat(invoiceDetails1).isNotEqualTo(invoiceDetails2);
        invoiceDetails1.setId(null);
        assertThat(invoiceDetails1).isNotEqualTo(invoiceDetails2);
    }
}
