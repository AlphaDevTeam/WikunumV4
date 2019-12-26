package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class ItemBinCardTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemBinCard.class);
        ItemBinCard itemBinCard1 = new ItemBinCard();
        itemBinCard1.setId(1L);
        ItemBinCard itemBinCard2 = new ItemBinCard();
        itemBinCard2.setId(itemBinCard1.getId());
        assertThat(itemBinCard1).isEqualTo(itemBinCard2);
        itemBinCard2.setId(2L);
        assertThat(itemBinCard1).isNotEqualTo(itemBinCard2);
        itemBinCard1.setId(null);
        assertThat(itemBinCard1).isNotEqualTo(itemBinCard2);
    }
}
