package com.bom.rentalmarket.chatting.config.kafka.constants;

import org.springframework.beans.factory.annotation.Value;

public class KafkaConstans {
    @Value("${spring.kafka.bootstrap-servers}")
    public static String bootstrapAddress;

    @Value("${spring.kafka.consumer.group-id}")
    public static String groupId;
}
