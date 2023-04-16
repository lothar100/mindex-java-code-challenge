package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.*;
import com.mindex.challenge.data.*;
import com.mindex.challenge.service.CompensationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

	@Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Reading compensation with employeeId [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null)
            throw new RuntimeException("Invalid employeeId: " + id);

        Compensation compensation = compensationRepository.findByEmployee(employee);
        if (compensation == null)
            throw new RuntimeException("Invalid employee: " + employee);

        return compensation;
    }

}