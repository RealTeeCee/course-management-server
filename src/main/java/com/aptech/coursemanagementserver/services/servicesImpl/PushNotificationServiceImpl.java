package com.aptech.coursemanagementserver.services.servicesImpl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.NotificationDto;
import com.aptech.coursemanagementserver.models.Notification;
import com.aptech.coursemanagementserver.repositories.NotificationRepository;
import com.aptech.coursemanagementserver.services.PushNotificationService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {

    private final NotificationRepository notificationRepository;

    public Flux<ServerSentEvent<List<NotificationDto>>> getNotificationsByUserToID(long userId) {

        if (userId == 0) {
            return Flux.interval(Duration.ofSeconds(1))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<NotificationDto>>builder().id(String.valueOf(sequence))
                            .event("user-list-event").data(getNotifs(userId))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<List<NotificationDto>>builder()
                .id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<>()).build());
    }

    private List<NotificationDto> getNotifs(long userId) {
        List<Notification> notifications = notificationRepository.findByUserToIdAndDeliveredFalse(userId);
        notifications.forEach(x -> x.setDelivered(true));
        notificationRepository.saveAll(notifications);

        List<NotificationDto> notificationDtos = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDto notificationDto = toDto(notification);
            notificationDtos.add(notificationDto);
        }

        return notificationDtos;
    }

    private NotificationDto toDto(Notification notification) {
        NotificationDto notifDto = NotificationDto.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .isDelivered(notification.isDelivered())
                .isRead(notification.isRead())
                .notificationType(notification.getNotificationType())
                .userFromId(notification.getUserFrom().getId())
                .userToId(notification.getUserTo().getId()).build();
        return notifDto;
    }
}
