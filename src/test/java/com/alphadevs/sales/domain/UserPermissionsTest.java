package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class UserPermissionsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPermissions.class);
        UserPermissions userPermissions1 = new UserPermissions();
        userPermissions1.setId(1L);
        UserPermissions userPermissions2 = new UserPermissions();
        userPermissions2.setId(userPermissions1.getId());
        assertThat(userPermissions1).isEqualTo(userPermissions2);
        userPermissions2.setId(2L);
        assertThat(userPermissions1).isNotEqualTo(userPermissions2);
        userPermissions1.setId(null);
        assertThat(userPermissions1).isNotEqualTo(userPermissions2);
    }
}
