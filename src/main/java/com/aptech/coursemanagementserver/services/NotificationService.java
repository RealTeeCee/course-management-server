package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.models.Notification;

public interface NotificationService {
    public Notification save(Notification notification);

    public Notification findById(long id);

    public List<Notification> findAllByUserIdNotRead(long userID);

    public List<Notification> findAllByUserId(long userID);

    public Notification updateStatusToRead(long id);

    public void clear();
}
