package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class DocumentNumberConfigTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentNumberConfig.class);
        DocumentNumberConfig documentNumberConfig1 = new DocumentNumberConfig();
        documentNumberConfig1.setId(1L);
        DocumentNumberConfig documentNumberConfig2 = new DocumentNumberConfig();
        documentNumberConfig2.setId(documentNumberConfig1.getId());
        assertThat(documentNumberConfig1).isEqualTo(documentNumberConfig2);
        documentNumberConfig2.setId(2L);
        assertThat(documentNumberConfig1).isNotEqualTo(documentNumberConfig2);
        documentNumberConfig1.setId(null);
        assertThat(documentNumberConfig1).isNotEqualTo(documentNumberConfig2);
    }
}
