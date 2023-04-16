package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
	
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Reading reporting structure with id [{}]", id);

        // recursively fill direct reports for all children,
        // otherwise direct reports of children are seen as null
        Employee employee = getEmployeeWithDirectReports(id);

        int numberOfReports = calcNumberOfReports(employee);

        return new ReportingStructure(employee, numberOfReports);
    }

    private Employee getEmployeeWithDirectReports(String id) {
        Employee currentEmployee = employeeRepository.findByEmployeeId(id);
        
        if(currentEmployee.getDirectReports() == null)
            return currentEmployee;

        for (Employee directReport : currentEmployee.getDirectReports()) {
            Employee currentDirectReport = getEmployeeWithDirectReports(directReport.getEmployeeId());
            directReport.copy(currentDirectReport);
        }

        return currentEmployee;
    }

    private int calcNumberOfReports(Employee employee) {
        if(employee.getDirectReports() == null)
            return 0;

        int numberOfReports = 0;
        for (Employee directReport : employee.getDirectReports())
            numberOfReports += calcNumberOfReports(directReport) + 1; // +1 for each directReport

        return numberOfReports;
    }

}