package kafka.new_producer.controller;

import kafka.new_producer.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("person")
@RequiredArgsConstructor
public class PersonController {

    private final static Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, Serializable> kafkaTemplateObject;

    @GetMapping()
    public ResponseEntity<String> producer(@RequestBody String message) {
        kafkaTemplate.send("topic-1", message);
        logger.info("Message delivered -> {}", message);
        return ResponseEntity.ok().body(message);
    }

    @GetMapping(value = "/teste-thread")
    public void produceThreadTest() {
        IntStream
                .range(0, 51)
                .boxed()
                .forEach(number -> kafkaTemplate.send("topic-1", number.toString()));

    }

    @GetMapping(value = "/send")
    public ResponseEntity<PersonDto> producerObj(@RequestBody PersonDto person) {
        kafkaTemplateObject.send("topic-person", person);
        logger.info("Message delivered -> {}", person);
        return ResponseEntity.ok().body(person);
    }

    @GetMapping(value = "/teste-partition")
    public void produceEspecificPartitionToBeConsumed() {
         kafkaTemplate.send("my-topic", "new teste partition");
    }
}
