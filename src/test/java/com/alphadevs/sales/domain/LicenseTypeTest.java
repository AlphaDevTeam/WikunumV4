package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class LicenseTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LicenseType.class);
        LicenseType licenseType1 = new LicenseType();
        licenseType1.setId(1L);
        LicenseType licenseType2 = new LicenseType();
        licenseType2.setId(licenseType1.getId());
        assertThat(licenseType1).isEqualTo(licenseType2);
        licenseType2.setId(2L);
        assertThat(licenseType1).isNotEqualTo(licenseType2);
        licenseType1.setId(null);
        assertThat(licenseType1).isNotEqualTo(licenseType2);
    }
}
