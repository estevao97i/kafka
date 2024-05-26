package kafka.producer.service;

import kafka.producer.dto.PessoaDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Value("${topic.kafka-jkl}")
    private String topicKafkaJkl;

    public String getTopicKafkaJkl() {
        return topicKafkaJkl;
    }

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, PessoaDto> kafkaTemplate1;

    public void sendMessage(String message) {
        kafkaTemplate.send(getTopicKafkaJkl(), message);
        logger.info("Mensagem => {}", message);
    }

    public void sendPessoa(PessoaDto pessoa) {
        kafkaTemplate1.send(getTopicKafkaJkl(), pessoa);
        logger.info("Pessoa => {}", pessoa);
    }
}
