package com.comunus.hrms.controller;

import java.io.FileOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.comunus.hrms.dao.EmsServiceCall;
import com.comunus.hrms.model.Jsonsecresp;
import com.comunus.hrms.model.NotificationDetails;
import com.comunus.hrms.security.WebAESEncryptionDecryption;
import com.comunus.hrms.ws.model.FireBaseResponseModel;
import com.comunus.hrms.ws.model.LeaveDetailsResponse;
import com.google.gson.Gson;

@Controller
public class NotificationController {
	
	private static final Logger logger = Logger.getLogger(NotificationController.class);

	@Autowired
	EmsServiceCall emsServiceCall;

	WebAESEncryptionDecryption aesEncDec = new WebAESEncryptionDecryption();
	WebAESEncryptionDecryption wencDrc = new WebAESEncryptionDecryption();

	@RequestMapping(value = "/NotificationDetails", method = RequestMethod.GET)
	public String ShowNotiflication(ModelMap model, Map<String, Object> model1,
			@RequestParam(required = false, defaultValue = "") String empLeaveDetails, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {

		logger.info("in Notification  Details");

		int companyId = Integer.parseInt(session.getAttribute("companyId").toString());
		int rm = Integer.parseInt(session.getAttribute("empId").toString());
		String empId = request.getParameter("eids");
		logger.info("eidssss" + empId);

		Jsonsecresp jsonsecresp = new Jsonsecresp();
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("companyId", companyId);
			jsonObj.put("rm", rm);
			jsonObj.put("empId", empId);

			jsonObj.put("empLeaveDetails", empLeaveDetails);
			jsonsecresp = (Jsonsecresp) emsServiceCall.callEMSReturn_HeaderObject(wencDrc.encrypt(jsonObj.toString()),
					"107", jsonsecresp);
			if (jsonsecresp.getStatus().getErrorCode().equals("0000")) {
				String decrypted = aesEncDec.decrypt(jsonsecresp.getJsonString());
				Gson gson = new Gson();
				LeaveDetailsResponse leaveDetailsResponse = gson.fromJson(decrypted, LeaveDetailsResponse.class);
				model.addAttribute("eid", leaveDetailsResponse.getEid());
				model.addAttribute("empId", leaveDetailsResponse.getEmpId());
				
				model.addAttribute("companyId", leaveDetailsResponse.getCompanyId());
				model.addAttribute("notificationList", leaveDetailsResponse.getEmpnotifylist());
				logger.info("List Of leaveDetailsResponse : " + leaveDetailsResponse);
			} else {
				model1.put("error", "Please Try After Some Time");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "notification";
	}

	@RequestMapping(value = "/processForm", method = RequestMethod.POST)
	public String Handleform(@RequestParam("image") MultipartFile image, @RequestParam("employeeId") String employeeId,
			@RequestParam("message") String message, @RequestParam("title") String title, Model model,
			HttpSession session, final RedirectAttributes redirectAttributes) {

		FireBaseResponseModel rest = new FireBaseResponseModel();
		try {

			System.out.println("File upload handler");
			System.out.println((image.getName()));
			System.out.println((image.getOriginalFilename()));

			byte[] data = image.getBytes();
			// save to server
			String path = "/home/comunus-user33/git/hrms/HRMS/src/main/webapp/resources/images/notification_img/"
					+ image.getOriginalFilename();
			System.out.println(path);
			String path1 = "resources/images/notification_img/" + image.getOriginalFilename();
			
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(data);
			fos.close();
			System.out.println("File Uploaded");

			int companyId = Integer.parseInt(session.getAttribute("companyId").toString());
			String createdBy = session.getAttribute("empId").toString();

			NotificationDetails notificationDetails = new NotificationDetails();
			notificationDetails.setEmployeeId(employeeId);
			notificationDetails.setCompanyId(companyId);
			notificationDetails.setImage(path1);
			notificationDetails.setMessage(message);
			notificationDetails.setData("");
			notificationDetails.setSendAll("");
			notificationDetails.setTitle(title);
			notificationDetails.setCreatedBy(createdBy);

			Jsonsecresp jsonsecresp = new Jsonsecresp();
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("firebaseDetails", notificationDetails);
			logger.info("List Of notification details  : " + notificationDetails);
			String reqParam = new Gson().toJson(notificationDetails);
			jsonsecresp = (Jsonsecresp) emsServiceCall.callEMSReturnObject(wencDrc.encrypt(reqParam), "104",
					jsonsecresp);
			if (jsonsecresp.getStatus().getErrorCode().equals("0000")) {
				String decrypted = aesEncDec.decrypt(jsonsecresp.getJsonString());
				Gson gson = new Gson();

				rest = gson.fromJson(decrypted, FireBaseResponseModel.class);
				logger.info("res :" + rest + "decrypted :" + decrypted);

				// model.addAttribute("notificationDetails", rest.getNotificationDetails());
				redirectAttributes.addFlashAttribute("message", "Message Sent Successfully.");

			} else {
				redirectAttributes.addFlashAttribute("message", " Unsuccessfully.");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/NotificationDetails";
	}

}