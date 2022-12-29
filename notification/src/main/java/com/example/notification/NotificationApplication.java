package com.example.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;

@SpringBootApplication
@Slf4j
public class NotificationApplication {
    @Autowired
    private EmailSenderService emailSenderService;
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic" , groupId = "notificationID")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        // send out an email notification
        log.info("Received Notification for Order - {}", orderPlacedEvent.getOrderNumber());
    }
    @EventListener(ApplicationReadyEvent.class)
    public void triggerMail() throws MessagingException {
        emailSenderService.sendSimpleEmail("rami.benarbi@enicar.ucar.tn",
                "your order is created with success",
                "we are pleased to inform you that your order has been shipped");
}}
