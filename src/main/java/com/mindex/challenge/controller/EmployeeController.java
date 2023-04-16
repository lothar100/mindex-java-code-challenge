package com.mindex.challenge.controller;

import com.mindex.challenge.data.*;
import com.mindex.challenge.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Autowired
    private CompensationService compensationService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    @GetMapping("/employee/reportingStructure/{id}")
    public ReportingStructure readReportingStructure(@PathVariable String id) {
        LOG.debug("Received reporting structure read request for id [{}]", id);

        return reportingStructureService.read(id);
    }

    @PostMapping("/employee/compensation")
    public Compensation createCompensation(@RequestBody Compensation compensation){
        LOG.debug("Received compensation create request for [{}]", compensation);

        return compensationService.create(compensation);
    }

    @GetMapping("/employee/compensation/{id}")
    public Compensation readCompensation(@PathVariable String id){
        LOG.debug("Received compensation read request for employeeId [{}]", id);

        return compensationService.read(id);
    }

}
