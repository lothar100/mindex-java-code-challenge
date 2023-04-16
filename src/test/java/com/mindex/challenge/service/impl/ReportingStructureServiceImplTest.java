package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureUrl;
    
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureUrl = "http://localhost:" + port +"/employee/reportingStructure/{id}";
    }

    @Test
    public void parameterizedTestRead() {
        // sorry, I couldn't figure out the proper way to parameterize this test :^)
        List<Object[]> parameters = Arrays.asList(
            new Object[][] {
                { "16a596ae-edd3-4847-99fe-c4518e82c86f", 4 },
                { "b7839309-3348-463b-a7e3-5de1c168beb3", 0 },
                { "03aa1462-ffa9-4978-901b-7c001562cf6f", 2 },
                { "62c1084e-6e34-4630-93fd-9153afb65309", 0 },
                { "c0c2293d-16bd-4603-8e08-638a9d18b22c", 0 }
            }
        );
        for (Object[] args : parameters) {
            testRead((String)args[0], (int)args[1]);
        }
    }

    private void testRead(String employeeId, int numberOfReports) {
        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, employeeId).getBody();
        assertEquals(employeeId, readReportingStructure.getEmployee().getEmployeeId());
        assertEquals(numberOfReports, readReportingStructure.getNumberOfReports());
    }

}