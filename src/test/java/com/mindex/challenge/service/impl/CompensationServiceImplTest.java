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
import java.util.*;
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
    public void parameterizedTestCreateRead() {
        // sorry, I couldn't figure out the proper way to parameterize this test :^)
        List<Object[]> parameters = Arrays.asList(
            new Object[][] {
                { employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f"), new BigDecimal(100000.00) },
                { employeeService.read("b7839309-3348-463b-a7e3-5de1c168beb3"), new BigDecimal(49000.00) },
                { employeeService.read("03aa1462-ffa9-4978-901b-7c001562cf6f"), new BigDecimal(12345.00) },
                { employeeService.read("62c1084e-6e34-4630-93fd-9153afb65309"), new BigDecimal(78654.00) },
                { employeeService.read("c0c2293d-16bd-4603-8e08-638a9d18b22c"), new BigDecimal(19283.00) }
            }
        );
        for (Object[] args : parameters) {
            testCreateRead((Employee)args[0], (BigDecimal)args[1]);
        }
    }

    private void testCreateRead(Employee testEmployee, BigDecimal testSalary) {
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployee(testEmployee);
        testCompensation.setSalary(testSalary);
        
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
