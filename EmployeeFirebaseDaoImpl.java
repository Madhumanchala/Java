package com.comunus.hrms.dbpersistency.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comunus.hrms.dbpersistency.entity.EmployeeFirebaseToken;

@Transactional
@Repository
public class EmployeeFirebaseDaoImpl implements EmployeeFirebaseDao {

	private static final Logger logger = Logger.getLogger(EmployeeFirebaseDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void insertfirebaseDetails(EmployeeFirebaseToken emptoken) {
		try {
			sessionFactory.getCurrentSession().save(emptoken);
			logger.info("Token inserted Successfully..!");
		} catch (Exception e) {
			logger.error("Exception inside the insertFirebase Details : " + e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public List<String> getAllToken() {
		List<String> list = null;
		try {
			Query query = sessionFactory.getCurrentSession().createSQLQuery("select token from EMS_MUST_NOTIFY_SYSTEM_DETAILS");
			list = query.list();
		} catch (Exception e) {
			logger.error("Exception in push : " + e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<String> getEmployeeTokenList(String employeeId, String companyId) {
		List<String> list = null;
		try {
			Query query = sessionFactory.getCurrentSession()
					.createSQLQuery("select token from EMS_MUST_NOTIFY_SYSTEM_DETAILS where employee_id= :employeeId and company_id= :companyId");
			query.setParameter("employeeId", employeeId);
			query.setParameter("companyId", companyId);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception inside EmployeeFirebase : "+e.getMessage());
		}
		return list;
	}

	@Override
	public List<String> getDetails(int employeeId, int companyId) {
		List<String> list = null;
		try {
			Query query = sessionFactory.getCurrentSession()
					.createSQLQuery("select token from EMS_MUST_NOTIFY_SYSTEM_DETAILS where employee_id= :employeeId and company_id= :companyId");
			query.setParameter("employeeId", employeeId);
			query.setParameter("companyId", companyId);
			list = query.list();
			logger.info("Token Updated Successfully..!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception inside EmployeeFirebase : "+e.getMessage());
		}
		return list;
	}

	public void updateEmpToken(EmployeeFirebaseToken firebase) {
		try {
			Query query=sessionFactory.getCurrentSession().createSQLQuery(
					"update EMS_MUST_NOTIFY_SYSTEM_DETAILS set device_type= :deviceType ,Status=:status, token=:token, Login_Datetime=:loginDate where employee_id=:empId and COMPANY_ID=:companyId");
			
			query.setParameter("empId", firebase.getEmployeeId());
			query.setParameter("companyId", firebase.getCompanyId());
			query.setParameter("deviceType", firebase.getDeviceType());
			query.setParameter("status", firebase.getStatus());
			query.setParameter("token", firebase.getToken());
			query.setParameter("loginDate", firebase.getLoginDate());
		} catch (Exception e) {
			logger.error("Exception inside the updateClient : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getEmployeeTokenList(int employeeId) {
		List<String> list = null;
		try {
			Query query = sessionFactory.getCurrentSession()
					.createSQLQuery("select token from EMS_MUST_NOTIFY_SYSTEM_DETAILS where employee_id= :employeeId");
			query.setParameter("employeeId", employeeId);
			list = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception inside EmployeeFirebase : "+e.getMessage());
		}
		return list;
	}
}
