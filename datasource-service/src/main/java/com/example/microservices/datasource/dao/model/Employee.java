package com.example.microservices.datasource.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @Column(name = "employeeNumber", nullable = false)
    private Long id;

    @Column
    @NotNull
    @Length(max = 50)
    private String lastName;

    @Column
    @NotNull
    @Length(max = 50)
    private String firstName;

    @Column
    @NotNull
    @Length(max = 10)
    private String extension;

    @Column
    @NotNull
    @Length(max = 100)
    private String email;

    @Column
    @NotNull
    @Length(max = 10)
    private String officeCode;

    @Column
    @NotNull
    private Long reportsTo;

    @Column
    @NotNull
    @Length(max = 50)
    private String jobTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    public Long getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(Long reportsTo) {
        this.reportsTo = reportsTo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
