package kafka.consumer.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.consumer.PessoaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);


    @KafkaListener(topics = "${topic.kafka-jkl}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) throws IOException {
        ObjectMapper obj = new ObjectMapper();
        PessoaDto pessoa = obj.readValue(message, PessoaDto.class);
        logger.info("Consumed message {}", pessoa);
    }
}
