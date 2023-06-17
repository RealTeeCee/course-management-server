package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.NotificationDto;
import com.aptech.coursemanagementserver.models.Notification;

public interface NotificationService {
    public Notification save(Notification notification);

    public NotificationDto findById(long id);

    public List<NotificationDto> findAllByUserIdNotRead(long userID);

    public List<NotificationDto> findAllByUserId(long userID);

    public NotificationDto updateStatusToRead(long id);

    public void clear();
}
