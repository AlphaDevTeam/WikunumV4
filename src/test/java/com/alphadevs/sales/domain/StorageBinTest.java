package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class StorageBinTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageBin.class);
        StorageBin storageBin1 = new StorageBin();
        storageBin1.setId(1L);
        StorageBin storageBin2 = new StorageBin();
        storageBin2.setId(storageBin1.getId());
        assertThat(storageBin1).isEqualTo(storageBin2);
        storageBin2.setId(2L);
        assertThat(storageBin1).isNotEqualTo(storageBin2);
        storageBin1.setId(null);
        assertThat(storageBin1).isNotEqualTo(storageBin2);
    }
}
