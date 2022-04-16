package com.comunus.hrms.firebase.serviceImpl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.comunus.hrms.common.security.WebAESEncryptionDecryption;
import com.comunus.hrms.common.utility.PojoToJson;
import com.comunus.hrms.dbpersistency.dao.EmployeeDao;
import com.comunus.hrms.dbpersistency.dao.EmployeeFirebaseDao;
import com.comunus.hrms.dbpersistency.dao.FirebaseDao;
import com.comunus.hrms.dbpersistency.dao.RolemasterDao;
import com.comunus.hrms.dbpersistency.entity.Employee;
import com.comunus.hrms.dbpersistency.entity.NotificationDetails;
import com.comunus.hrms.dbpersistency.entity.RoleMaster;
import com.comunus.hrms.dbpersistency.entity.SchedulerModelDetails;
import com.comunus.hrms.firebase.message.FirebaseMessagingService;
import com.comunus.hrms.service.model.Jsonsecresp;
import com.comunus.hrms.service.model.Status;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class EmployeeFirebaseServiceImpl {

	@Autowired
	FirebaseMessagingService firebaseMessagingService;

	@Autowired
	EmployeeDao employeeDao;

	@Autowired
	RolemasterDao rolemasterDao;
	@Autowired
	FirebaseDao firebaseDao;
	@Autowired
	EmployeeFirebaseDao employeeFirebaseDao;

	private static final Logger logger = Logger.getLogger(EmployeeFirebaseServiceImpl.class);
	WebAESEncryptionDecryption wencDrc = new WebAESEncryptionDecryption();

	public Jsonsecresp pushNotification(String reqParam) {
		Jsonsecresp json = new Jsonsecresp();
		String companyId = "";
		String employeeId = "";
		String message = "";
		String title = "";
		String image = "";
		String sendAll = "";
		String data = "";
		String createdBy = "";
		try {
			if (reqParam != null && !reqParam.equals("")) {
				reqParam = wencDrc.decrypt(reqParam);
				logger.info("Request string for push Notification : " + reqParam);
				Gson gson = new Gson();
				JsonElement element = gson.fromJson(reqParam, JsonElement.class);
				JsonObject obj = element.getAsJsonObject();
				if (obj.get("createdBy") != null) {
					createdBy = obj.get("createdBy").getAsString();
				}
				if (obj.has("companyId")) {
					if (obj.get("companyId") != null) {
						companyId = obj.get("companyId").getAsString();
					} else {
						json.setStatus(new Status("0001", "Invalid Company ID"));
					}
				} else {
					json.setStatus(new Status("0002", "Company ID missing"));
				}
				if (obj.get("employeeId") != null) {
					employeeId = obj.get("employeeId").getAsString();
				} else {
					json.setStatus(new Status("0003", "Invalid Employee ID"));
				}
				if (obj.get("message") != null) {
					message = obj.get("message").getAsString();
				}
				if (obj.get("title") != null) {
					title = obj.get("title").getAsString();
				}
				if (obj.get("data") != null) {
					data = obj.get("data").getAsString();
				}
				if (obj.get("image") != null) {
					image = obj.get("image").getAsString();
				}
				NotificationDetails notificationDetails = new NotificationDetails();
				List<Employee> rm = employeeDao.findByWhereClause(Integer.parseInt(createdBy),
						Integer.parseInt(companyId));

				String rmName = rm.get(0).getFirst_name() + " " + rm.get(0).getLast_name();
				
				/*
				 * UriComponents uri = UriComponentsBuilder .fromHttpUrl("http://image")
				 * .buildAndExpand("", "", "");
				 * 
				 * String urlString = uri.toUriString();
				 */
	
				
				notificationDetails.setData(data);
				notificationDetails.setMessage(message);
				notificationDetails.setImage(image);
				notificationDetails.setTitle(title);
				notificationDetails.setCreatedDate(new Date());
				notificationDetails.setId(Integer.parseInt(companyId));
				notificationDetails.setCreatedBy(rmName);
				if (employeeId.equalsIgnoreCase("ALL")) {
					List<String> list = employeeFirebaseDao.getAllToken();
					for (int i = 0; i < list.size(); i++) {
						String msgId = firebaseMessagingService.sendNotification(notificationDetails, list.get(i));
						logger.info("fire notification success...!");
						notificationDetails.setNotifyStatus("Success");
						notificationDetails.setSendAll(sendAll);
					}

					firebaseDao.insertfirebaseDetails(notificationDetails);

					logger.info("inserted success...!");
				} else {

					List<String> list = employeeFirebaseDao.getEmployeeTokenList(employeeId, companyId);
					if (list != null && !list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							notificationDetails.setSendAll(employeeId);
							String msgId = firebaseMessagingService.sendNotification(notificationDetails, list.get(i));
							notificationDetails.setNotifyStatus("Success");
							json.setStatus(new Status("0000", "Sended notification success"));
						}
						firebaseDao.insertfirebaseDetails(notificationDetails);
					} else {
						json.setStatus(new Status("1111", "Notification send failed -Token null-"));
					}
				}

				json.setJsonString(wencDrc.encrypt(PojoToJson.PojotoJson(notificationDetails)));
				json.setStatus(new Status("0000", "SUCCESS"));
			}

		}catch (FirebaseMessagingException e) {
			logger.error("Exception inside firebase Token expired:" + e.getMessage());
			json.setStatus(new Status("1111", "Notification send failed -Token Expired-"));
		} 
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception inside firebase" + e.getMessage());
			json.setStatus(new Status("1111", "Notification send failed"));
		}
		return json;

	}
	
	
	public Jsonsecresp pushNotificationThoughScheduler(SchedulerModelDetails details) {
		Jsonsecresp json = new Jsonsecresp();
		try {
			if (details != null) {
				
				NotificationDetails notificationDetails = new NotificationDetails();
				notificationDetails.setData(details.getData());
				notificationDetails.setMessage(details.getMessage());
				notificationDetails.setImage(details.getImage());
				notificationDetails.setTitle(details.getTitle());
				notificationDetails.setCreatedDate(new Date());
				notificationDetails.setId(details.getCompanyId());
				notificationDetails.setCreatedBy("JOB SCHEDULER");
					List<String> list = employeeFirebaseDao.getEmployeeTokenList(details.getEmployeeId());
					if (list != null && !list.isEmpty()) {
						for (int i = 0; i < list.size(); i++) {
							String msgId = firebaseMessagingService.sendNotification(notificationDetails, list.get(i));
							notificationDetails.setNotifyStatus("Success");
							json.setStatus(new Status("0000", "Sended notification success"));
						}
						firebaseDao.insertfirebaseDetails(notificationDetails);
					} else {
						json.setStatus(new Status("1111", "Notification send failed -Token null-"));
					}

				json.setJsonString(wencDrc.encrypt(PojoToJson.PojotoJson(notificationDetails)));
				json.setStatus(new Status("0000", "SUCCESS"));
			}

		}catch (FirebaseMessagingException e) {
			logger.error("Exception inside firebase Token expired:" + e.getMessage());
			json.setStatus(new Status("1111", "Notification send failed -Token Expired-"));
		} 
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception inside firebase" + e.getMessage());
			json.setStatus(new Status("1111", "Notification send failed"));
		}
		return json;

	}
}
