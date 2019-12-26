package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class DocumentHistoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentHistory.class);
        DocumentHistory documentHistory1 = new DocumentHistory();
        documentHistory1.setId(1L);
        DocumentHistory documentHistory2 = new DocumentHistory();
        documentHistory2.setId(documentHistory1.getId());
        assertThat(documentHistory1).isEqualTo(documentHistory2);
        documentHistory2.setId(2L);
        assertThat(documentHistory1).isNotEqualTo(documentHistory2);
        documentHistory1.setId(null);
        assertThat(documentHistory1).isNotEqualTo(documentHistory2);
    }
}
