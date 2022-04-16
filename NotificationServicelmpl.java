package com.comunus.hrms.serviceImpl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.comunus.hrms.common.security.WebAESEncryptionDecryption;
import com.comunus.hrms.common.utility.PojoToJson;
import com.comunus.hrms.dbpersistency.dao.EmployeeDao;
import com.comunus.hrms.dbpersistency.dao.EmployeeDetailDao;
import com.comunus.hrms.dbpersistency.dao.FirebaseDao;
import com.comunus.hrms.dbpersistency.dao.TimesheetDao;
import com.comunus.hrms.dbpersistency.entity.NotificationDetails;
import com.comunus.hrms.dbpersistency.entity.SchedulerModelDetails;
import com.comunus.hrms.firebase.serviceImpl.EmployeeFirebaseServiceImpl;
import com.comunus.hrms.service.model.Jsonsecresp;
import com.comunus.hrms.service.model.LeaveDetailsResponse;
import com.comunus.hrms.service.model.Status;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class NotificationServicelmpl {

	private static final Logger logger = Logger.getLogger(NotificationServicelmpl.class);
	WebAESEncryptionDecryption wencDrc = new WebAESEncryptionDecryption();

	@Autowired
	EmployeeFirebaseServiceImpl employeeFirebaseServiceImpl;
	
	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private EmployeeDetailDao empdetailDao;

	@Autowired
	private FirebaseDao firebaseDao;

	@Autowired
	private TimesheetDao timesheetDao;
	
	@Scheduled(cron = "0 0 10,19 * * *")
	// To send system generated notification to employees those who not filled the TimeSheet.
	public String systemSchedulerFirebaseNotify() {

		try {
			LocalDate date = LocalDate.now();
			LocalDate date1 = LocalDate.now().minusDays(3);// 2022-04-13
			LocalDate date2 = LocalDate.now().minusDays(4);// 2022-04-12
			LocalDate date3 = LocalDate.now().minusDays(5);// 2022-04-11
//			int companyId = 1;
			List<com.comunus.hrms.dbpersistency.entity.Employee> empList = firebaseDao
					.findByEmpTimesheetDetailsnotfilled(date1);
			
			for (com.comunus.hrms.dbpersistency.entity.Employee empDetails : empList) {
				SchedulerModelDetails notificationDetails = new SchedulerModelDetails();
				notificationDetails.setData("HRMS BUDDY");
				notificationDetails.setMessage("PLEASE FILL YOUR 3 DAYS TIMESHEET");
				notificationDetails.setImage("");
				notificationDetails.setTitle("TITLE");
				notificationDetails.setCreatedDate(new Date());
				notificationDetails.setCompanyId(1);
				notificationDetails.setCreatedBy("SCHEDULER");
				notificationDetails.setEmployeeId(empDetails.getE_id());
				employeeFirebaseServiceImpl.pushNotificationThoughScheduler(notificationDetails);
			}
			
			List<com.comunus.hrms.dbpersistency.entity.Employee> empList1 = firebaseDao
					.findByEmpTimesheetDetailsnotfilled1(date2);
			
			for (com.comunus.hrms.dbpersistency.entity.Employee empDetails : empList1) {
				SchedulerModelDetails notificationDetails = new SchedulerModelDetails();
				notificationDetails.setData("HRMS BUDDY");
				notificationDetails.setMessage("PLEASE FILL YOUR 4 DAYS TIMESHEET");
				notificationDetails.setImage("");
				notificationDetails.setTitle("TITLE");
				notificationDetails.setCreatedDate(new Date());
				notificationDetails.setCompanyId(1);
				notificationDetails.setCreatedBy("SCHEDULER");
				notificationDetails.setEmployeeId(empDetails.getE_id());
				employeeFirebaseServiceImpl.pushNotificationThoughScheduler(notificationDetails);
			}
			
			List<com.comunus.hrms.dbpersistency.entity.Employee> empList2 = firebaseDao
					.findByEmpTimesheetDetailsnotfilled2(date3);
			
			for (com.comunus.hrms.dbpersistency.entity.Employee empDetails : empList2) {
				SchedulerModelDetails notificationDetails = new SchedulerModelDetails();
				notificationDetails.setData("HRMS BUDDY");
				notificationDetails.setMessage("PLEASE FILL YOUR 5 DAYS TIMESHEET");
				notificationDetails.setImage("");
				notificationDetails.setTitle("TITLE");
				notificationDetails.setCreatedDate(new Date());
				notificationDetails.setCompanyId(1);
				notificationDetails.setCreatedBy("SCHEDULER");
				notificationDetails.setEmployeeId(empDetails.getE_id());
				employeeFirebaseServiceImpl.pushNotificationThoughScheduler(notificationDetails);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception inside empList " + e.getMessage());
		}
		System.out.println("SCHEDULER  CALL");
		return "";
	}
	
	
	public Jsonsecresp getNotifyEmployeeList(String reqParam) {

		String companyId = "";
		String rm = "";
		String empId = "";

		Jsonsecresp json = new Jsonsecresp();
		LeaveDetailsResponse resModel = new LeaveDetailsResponse();
		try {
			if (reqParam != null && !reqParam.equals("")) {
				reqParam = wencDrc.decrypt(reqParam);

				logger.info("Request string for leaveCompOffDetails : " + reqParam);
				Gson gson = new Gson();
				JsonElement element = gson.fromJson(reqParam, JsonElement.class);
				JsonObject obj = element.getAsJsonObject();
				if (obj.has("empId")) {
					if (obj.get("empId") != null) {
						empId = obj.get("empId").getAsString();
					} else {
						json.setStatus(new Status("0001", "Invalid employee ID"));
					}
				} else {
					json.setStatus(new Status("0002", "employee ID missing"));
				}
				if (obj.has("companyId")) {
					if (obj.get("companyId") != null) {
						companyId = obj.get("companyId").getAsString();
					} else {
						json.setStatus(new Status("0001", "Invalid company ID"));
					}
				} else {
					json.setStatus(new Status("0002", "company ID missing"));
				}

				rm = obj.get("rm").getAsString();
				if (obj.has("rm")) {
					if (obj.get("rm") != null) {
						rm = obj.get("rm").getAsString();
					} else {
						json.setStatus(new Status("0001", "Invalid rm"));
					}
				} else {
					json.setStatus(new Status("0002", "rm missing"));
				}

				List<com.comunus.hrms.service.model.Employee> eid = firebaseDao
						.employeeViewforNotify(Integer.parseInt(companyId));

				List<NotificationDetails> empnotifylist = firebaseDao.getEmpList();

				logger.info("we are chec");
				resModel.setEid(eid);
				resModel.setEmpnotifylist(empnotifylist);
				resModel.setEmpId(empId);
				resModel.setCompanyId(companyId);

				logger.info("Response is  ok " + resModel);
				json.setJsonString(wencDrc.encrypt(PojoToJson.PojotoJson(resModel)));
				json.setStatus(new Status("0000", "SUCCESS"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setStatus(new Status("9999", "FAIL"));
		}

		return json;
	}
}
