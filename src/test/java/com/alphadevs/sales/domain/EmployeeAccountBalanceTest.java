package com.alphadevs.sales.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.alphadevs.sales.web.rest.TestUtil;

public class EmployeeAccountBalanceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeAccountBalance.class);
        EmployeeAccountBalance employeeAccountBalance1 = new EmployeeAccountBalance();
        employeeAccountBalance1.setId(1L);
        EmployeeAccountBalance employeeAccountBalance2 = new EmployeeAccountBalance();
        employeeAccountBalance2.setId(employeeAccountBalance1.getId());
        assertThat(employeeAccountBalance1).isEqualTo(employeeAccountBalance2);
        employeeAccountBalance2.setId(2L);
        assertThat(employeeAccountBalance1).isNotEqualTo(employeeAccountBalance2);
        employeeAccountBalance1.setId(null);
        assertThat(employeeAccountBalance1).isNotEqualTo(employeeAccountBalance2);
    }
}
