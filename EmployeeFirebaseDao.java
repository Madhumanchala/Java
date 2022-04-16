package com.comunus.hrms.dbpersistency.dao;

import java.util.List;

import com.comunus.hrms.dbpersistency.entity.EmployeeFirebaseToken;
public interface EmployeeFirebaseDao {

	public void insertfirebaseDetails(EmployeeFirebaseToken emptoken);

	public List<String> getEmployeeTokenList(String employeeId, String companyId);
	public List<String> getEmployeeTokenList(int employeeId);
	public List<String> getAllToken();

	public List<String> getDetails(int empId, int compId);
	
	public void updateEmpToken(EmployeeFirebaseToken firebase);
}
