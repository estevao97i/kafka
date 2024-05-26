package kafka.consumer_01.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.consumer_01.dto.PessoaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class Consumer01 {

    private final static Logger logger = LoggerFactory.getLogger(Consumer01.class);

    @KafkaListener(topics = "${topic.kafka-jkl}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumer01(String message,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                           @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) throws JsonProcessingException {
        ObjectMapper objMapper = new ObjectMapper();
        PessoaDto pessoa = objMapper.readValue(message, PessoaDto.class);
        logger.info("Mensagem do consumer 2: {}, no topico: {}, na partição {}", pessoa, topic, partition);

    }

}
