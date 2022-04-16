package com.comunus.hrms.dbpersistency.dao;

import java.time.LocalDate;
import java.util.List;

import com.comunus.hrms.dbpersistency.entity.EmpTimesheetDetails;
import com.comunus.hrms.dbpersistency.entity.NotificationDetails;


import com.comunus.hrms.service.model.Employee;


public interface FirebaseDao {
	
	public void insertfirebaseDetails(NotificationDetails notificationDetails);
	
	public List<com.comunus.hrms.service.model.Employee> employeeViewforNotify(int companyID);

	List<NotificationDetails> getEmpList();

	public List<com.comunus.hrms.dbpersistency.entity.Employee> findByEmpTimesheetDetailsnotfilled(LocalDate date1);
	
	public List<com.comunus.hrms.dbpersistency.entity.Employee> findByEmpTimesheetDetailsnotfilled1(LocalDate date2);
	
	public List<com.comunus.hrms.dbpersistency.entity.Employee> findByEmpTimesheetDetailsnotfilled2(LocalDate date3);

}
