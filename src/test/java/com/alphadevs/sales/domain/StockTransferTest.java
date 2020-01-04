package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class StockTransferTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockTransfer.class);
        StockTransfer stockTransfer1 = new StockTransfer();
        stockTransfer1.setId(1L);
        StockTransfer stockTransfer2 = new StockTransfer();
        stockTransfer2.setId(stockTransfer1.getId());
        assertThat(stockTransfer1).isEqualTo(stockTransfer2);
        stockTransfer2.setId(2L);
        assertThat(stockTransfer1).isNotEqualTo(stockTransfer2);
        stockTransfer1.setId(null);
        assertThat(stockTransfer1).isNotEqualTo(stockTransfer2);
    }
}
