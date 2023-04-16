package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;
    
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/employee/compensation";
        compensationIdUrl = "http://localhost:" + port + "/employee/compensation/{id}";
    }

    @Test
    public void testCreateRead() {
        Employee testEmployee = employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");

        Compensation testCompensation = new Compensation();
        testCompensation.setEmployee(testEmployee);
        testCompensation.setSalary(new BigDecimal(100000.00));
        
        long day = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        Date expiry = new Date(System.currentTimeMillis() + day);
        testCompensation.setEffectiveDate(expiry);

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
        assertNotNull(createdCompensation.getEmployee());
        assertNotNull(createdCompensation.getSalary());
        assertNotNull(createdCompensation.getEffectiveDate());
        assertCompensationEquivalence(testCompensation, createdCompensation);

        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();
        assertCompensationEquivalence(createdCompensation, readCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        DateFormat converter = new SimpleDateFormat("yyyy-MM-dd");
        converter.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(converter.format(expected.getEffectiveDate()), converter.format(actual.getEffectiveDate()));
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
    
}
