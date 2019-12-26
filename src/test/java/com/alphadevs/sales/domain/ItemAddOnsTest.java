package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class ItemAddOnsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemAddOns.class);
        ItemAddOns itemAddOns1 = new ItemAddOns();
        itemAddOns1.setId(1L);
        ItemAddOns itemAddOns2 = new ItemAddOns();
        itemAddOns2.setId(itemAddOns1.getId());
        assertThat(itemAddOns1).isEqualTo(itemAddOns2);
        itemAddOns2.setId(2L);
        assertThat(itemAddOns1).isNotEqualTo(itemAddOns2);
        itemAddOns1.setId(null);
        assertThat(itemAddOns1).isNotEqualTo(itemAddOns2);
    }
}
