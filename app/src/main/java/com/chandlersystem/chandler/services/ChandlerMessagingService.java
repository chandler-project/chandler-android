package com.chandlersystem.chandler.services;

import com.chandlersystem.chandler.utilities.LogUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ChandlerMessagingService extends FirebaseMessagingService {
    private static final String TAG = ChandlerMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        LogUtil.logI(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        // TODO: Handle message containing extra data here
        if (remoteMessage.getData().size() > 0) {
            LogUtil.logI(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        // TODO Handle message containing string only
        if (remoteMessage.getNotification() != null) {
            LogUtil.logI(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
