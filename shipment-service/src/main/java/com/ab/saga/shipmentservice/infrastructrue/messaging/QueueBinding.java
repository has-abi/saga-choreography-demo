package com.ab.saga.shipmentservice.infrastructrue.messaging;

import lombok.Data;

@Data
public class QueueBinding {
    private String queue;
    private String exchange;
    private String routingKey;
}
