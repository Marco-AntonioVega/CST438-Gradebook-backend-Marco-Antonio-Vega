package com.cst438.domain;

import java.sql.Date;

public class GradebookParamsObject {
	private String name;
	private Date dueDate;
	
	public String getName() {
		return name;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}