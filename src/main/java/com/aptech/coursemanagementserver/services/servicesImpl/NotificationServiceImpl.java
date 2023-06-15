// package com.aptech.coursemanagementserver.services.servicesImpl;

// import java.util.List;
// import java.util.NoSuchElementException;

// import org.springframework.stereotype.Service;

// import com.aptech.coursemanagementserver.models.Notification;
// import com.aptech.coursemanagementserver.repositories.NotificationRepository;
// import com.aptech.coursemanagementserver.services.NotificationService;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class NotificationServiceImpl implements NotificationService {
// private final NotificationRepository notifRepository;

// public Notification save(Notification notification) {
// return notifRepository.save(notification);
// }

// public Notification findById(long id) {
// return notifRepository.findById(id).orElseThrow(() -> new
// NoSuchElementException(
// "The notification with notificationId: [" + id + "] is not exist."));
// }

// public List<Notification> findAllByUserIdNotRead(long userID) {
// return notifRepository.findByUserToIdAndDeliveredFalse(userID);
// }

// public List<Notification> findAllByUserId(long userID) {
// return notifRepository.findByUserToId(userID);
// }

// public Notification updateStatusToRead(long id) {
// var notif = notifRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException(
// "The notification with notificationId: [" + id + "] is not exist."));
// notif.setRead(true);
// return notifRepository.save(notif);
// }

// public void clear() {
// notifRepository.deleteAll();
// }
// }
