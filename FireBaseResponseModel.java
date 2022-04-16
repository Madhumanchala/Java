package com.comunus.hrms.ws.model;

import java.util.ArrayList;
import com.comunus.hrms.model.NotificationDetails;

public class FireBaseResponseModel {

	private ArrayList<NotificationDetails> notificationDetails;

	public ArrayList<NotificationDetails> getNotificationDetails() {
		return notificationDetails;
	}

	public void setNotificationDetails(ArrayList<NotificationDetails> notificationDetails) {
		this.notificationDetails = notificationDetails;
	}

}