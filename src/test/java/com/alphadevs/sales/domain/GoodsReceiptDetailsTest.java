package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class GoodsReceiptDetailsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GoodsReceiptDetails.class);
        GoodsReceiptDetails goodsReceiptDetails1 = new GoodsReceiptDetails();
        goodsReceiptDetails1.setId(1L);
        GoodsReceiptDetails goodsReceiptDetails2 = new GoodsReceiptDetails();
        goodsReceiptDetails2.setId(goodsReceiptDetails1.getId());
        assertThat(goodsReceiptDetails1).isEqualTo(goodsReceiptDetails2);
        goodsReceiptDetails2.setId(2L);
        assertThat(goodsReceiptDetails1).isNotEqualTo(goodsReceiptDetails2);
        goodsReceiptDetails1.setId(null);
        assertThat(goodsReceiptDetails1).isNotEqualTo(goodsReceiptDetails2);
    }
}
