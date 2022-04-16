package com.comunus.hrms.dbpersistency.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comunus.hrms.dbpersistency.entity.EmpTimesheetDetails;
import com.comunus.hrms.dbpersistency.entity.NotificationDetails;
import com.comunus.hrms.service.model.Employee;
import com.comunus.hrms.dbpersistency.entity.NotificationDetails;

@Repository
@Transactional
public class FirebaseDaoImpl implements FirebaseDao {

	private static final Logger logger = Logger.getLogger(FirebaseDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public void insertfirebaseDetails(NotificationDetails notificationDetails) {
		try {
			sessionFactory.getCurrentSession().save(notificationDetails);
//			sessionFactory.getCurrentSession();
//			sessionFactory.isOpen();
			sessionFactory.openSession();
		} catch (Exception e) {
			logger.error("Exception inside the insert notify : " + e.getMessage());
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public List<com.comunus.hrms.service.model.Employee> employeeViewforNotify(int companyID) {
		List<Object[]> emplist = new ArrayList<Object[]>();
		List<com.comunus.hrms.service.model.Employee> employee = new ArrayList<com.comunus.hrms.service.model.Employee>();
		try {

			String sqldefaulkey = "Select v.EMP_ID,v.FIRST_NAME,v.LAST_NAME,v.EMP_DOJ from  VIEW_EMPLOYEE_DETAILS v where EMP_status = 'Active' and COMPANY_ID= :companyID";

			// String sqldefaulkey = "Select v.EMP_ID,v.FIRST_NAME,v.LAST_NAME,v.EMP_DOJ
			// from VIEW_EMPLOYEE_DETAILS v INNER JOIN EMS_MUST_NOTIFY_SYSTEM_DETAILS s ON
			// v.EMP_ID = s.employee_id where EMP_status = 'Active' and
			// v.COMPANY_ID=:companyID and token=token";
			Query query = sessionFactory.getCurrentSession().createSQLQuery(sqldefaulkey);
			query.setParameter("companyID", companyID);
			emplist = query.list();
			if (emplist.size() > 0) {

				for (Object[] empl : emplist) {
					com.comunus.hrms.service.model.Employee employeemodel = new com.comunus.hrms.service.model.Employee();
					employeemodel.setE_id(String.valueOf(empl[0]));
					employeemodel.setFirst_name(String.valueOf(empl[1]));
					employeemodel.setLast_name(String.valueOf(empl[2]));
					employeemodel.setDoj(String.valueOf(empl[3]));
					employee.add(employeemodel);
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside the employeeViewforNotify : " + e.getMessage());
			e.printStackTrace();
		}
		if (!emplist.isEmpty()) {
			return Collections.unmodifiableList(employee);
		} else {
			return Collections.<com.comunus.hrms.service.model.Employee>emptyList();
		}
	}

	public List<NotificationDetails> getEmpList() {
		// TODO Auto-generated method stub
		List<NotificationDetails> list = null;
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("from NotificationDetails ");
			// query.setParameter("companyId", companyId);
			list = query.list();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.info("Exception inside getNotificationDetails : " + e.getMessage());
		}
		return list;
	}

	// ------------------- new CODE ADDED HERE

	@SuppressWarnings("unchecked")
	public List<com.comunus.hrms.dbpersistency.entity.Employee> findByEmpTimesheetDetailsnotfilled(LocalDate date1) {
		List<com.comunus.hrms.dbpersistency.entity.Employee> list = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"from Employee where EMP_ID not IN (SELECT empId FROM EmpTimesheetDetails where TIMESHEET_DATE in (:date))");
			query.setParameter("date", date1);
			list = query.list();
		} catch (Exception e) {
			logger.error("Exception inside the findByEmpTimesheetDetails : " + e.getMessage());
			e.printStackTrace();
		}

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<com.comunus.hrms.dbpersistency.entity.Employee> findByEmpTimesheetDetailsnotfilled1(LocalDate date2) {
		List<com.comunus.hrms.dbpersistency.entity.Employee> list = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"from Employee where EMP_ID not IN (SELECT empId FROM EmpTimesheetDetails where TIMESHEET_DATE in (:date))");
			query.setParameter("date", date2);
			list = query.list();
		} catch (Exception e) {
			logger.error("Exception inside the findByEmpTimesheetDetails : " + e.getMessage());
			e.printStackTrace();
		}

		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<com.comunus.hrms.dbpersistency.entity.Employee> findByEmpTimesheetDetailsnotfilled2(LocalDate date3) {
		List<com.comunus.hrms.dbpersistency.entity.Employee> list = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"from Employee where EMP_ID not IN (SELECT empId FROM EmpTimesheetDetails where TIMESHEET_DATE in (:date))");
			query.setParameter("date", date3);
			list = query.list();
		} catch (Exception e) {
			logger.error("Exception inside the findByEmpTimesheetDetails : " + e.getMessage());
			e.printStackTrace();
		}

		return list;
	}



}
