package com.mindex.challenge.data;

import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Compensation {
	private Employee employee;
    private BigDecimal salary;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date effectiveDate;

    public Compensation() {
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(effectiveDate);

        this.effectiveDate = calendar.getTime();
    }

}