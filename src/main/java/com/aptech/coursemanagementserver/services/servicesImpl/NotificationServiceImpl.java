package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.NotificationDto;
import com.aptech.coursemanagementserver.dtos.UserDto;
import com.aptech.coursemanagementserver.models.Notification;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.NotificationRepository;
import com.aptech.coursemanagementserver.services.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notifRepository;

    public Notification save(Notification notification) {
        return notifRepository.save(notification);
    }

    public NotificationDto findById(long id) {
        Notification notification = notifRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "The notification with notificationId: [" + id + "] is not exist."));
        return toNotifDto(notification);
    }

    public List<NotificationDto> findAllByUserIdNotRead(long userID) {
        List<Notification> notifications = notifRepository.findByUserToIdAndDeliveredFalse(userID);

        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDto notificationDto = toNotifDto(notification);
            notificationDtos.add(notificationDto);
        }

        return notificationDtos;
    }

    public List<NotificationDto> findAllByUserId(long userID) {
        List<Notification> notifications = notifRepository.findByUserToId(userID);

        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDto notificationDto = toNotifDto(notification);
            notificationDtos.add(notificationDto);
        }

        return notificationDtos;
    }

    public NotificationDto updateStatusToRead(long id) {
        var notif = notifRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "The notification with notificationId: [" + id + "] is not exist."));
        notif.setRead(true);
        notifRepository.save(notif);

        NotificationDto notificationDto = toNotifDto(notif);
        return notificationDto;
    }

    public void clear() {
        notifRepository.deleteAll();
    }

    private NotificationDto toNotifDto(Notification notification) {
        User userFrom = notification.getUserFrom();
        UserDto userFromDto = toUserDto(userFrom);

        User userTo = notification.getUserTo();
        UserDto userToDto = toUserDto(userTo);

        NotificationDto notifDto = NotificationDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .isDelivered(notification.isDelivered())
                .isRead(notification.isRead())
                .notificationType(notification.getNotificationType())
                .userFrom(userFromDto)
                .userTo(userToDto).build();
        return notifDto;
    }

    private UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .imageUrl(user.getImageUrl())
                .role(user.getRole())
                .build();
        return userDto;
    }
}
