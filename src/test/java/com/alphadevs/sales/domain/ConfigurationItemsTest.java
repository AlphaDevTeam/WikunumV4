package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class ConfigurationItemsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationItems.class);
        ConfigurationItems configurationItems1 = new ConfigurationItems();
        configurationItems1.setId(1L);
        ConfigurationItems configurationItems2 = new ConfigurationItems();
        configurationItems2.setId(configurationItems1.getId());
        assertThat(configurationItems1).isEqualTo(configurationItems2);
        configurationItems2.setId(2L);
        assertThat(configurationItems1).isNotEqualTo(configurationItems2);
        configurationItems1.setId(null);
        assertThat(configurationItems1).isNotEqualTo(configurationItems2);
    }
}
