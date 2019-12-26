package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class GoodsReceiptTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoodsReceipt.class);
        GoodsReceipt goodsReceipt1 = new GoodsReceipt();
        goodsReceipt1.setId(1L);
        GoodsReceipt goodsReceipt2 = new GoodsReceipt();
        goodsReceipt2.setId(goodsReceipt1.getId());
        assertThat(goodsReceipt1).isEqualTo(goodsReceipt2);
        goodsReceipt2.setId(2L);
        assertThat(goodsReceipt1).isNotEqualTo(goodsReceipt2);
        goodsReceipt1.setId(null);
        assertThat(goodsReceipt1).isNotEqualTo(goodsReceipt2);
    }
}
