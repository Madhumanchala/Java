package com.comunus.hrms.firebase.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comunus.hrms.dbpersistency.entity.NotificationDetails;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseMessagingService {

	@Autowired(required = true)
	FirebaseMessaging firebaseMessaging;

	public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
		this.firebaseMessaging = firebaseMessaging;
	}

	public String sendNotification(NotificationDetails notificationDetails, String token)
			throws FirebaseMessagingException { 
		Notification notification = Notification.builder().setTitle(notificationDetails.getTitle())
				.setBody(notificationDetails.getMessage()).setImage(notificationDetails.getImage()).build();

//		  Notification notification =
//		  Notification.builder().setTitle(notificationDetails.getTitle()).setBody(
//		  notificationDetails.getMessage()) .setImage(
//		  "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg")
//		  .build();

		Message message = Message.builder().setToken(token).setNotification(notification)
				.putData("data", notificationDetails.getData()).build();

		return firebaseMessaging.send(message);
	}

}