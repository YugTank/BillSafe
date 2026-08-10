package com.billsafe.billsafe.purchase.producer;

import com.billsafe.billsafe.purchase.event.PurchaseCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PurchaseEventProducer {

    private final KafkaTemplate<String, PurchaseCreatedEvent> kafkaTemplate;

    public PurchaseEventProducer(KafkaTemplate<String, PurchaseCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void purchaseCreated(PurchaseCreatedEvent purchaseCreatedEvent) {
        kafkaTemplate.send("purchase-created", purchaseCreatedEvent.getPurchaseId().toString(), purchaseCreatedEvent);
    }
}
