package kafka.new_consumer.listener;

import kafka.new_consumer.custom.PersonCustomListener;
import kafka.new_consumer.dto.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class Consumer {

    @KafkaListener(topics = "topic-1", groupId = "group_id", concurrency = "2")
    public void listener(String message) {
        log.info("Thread: {}", Thread.currentThread().getId());
        log.info("Received: {}", message);
    }

    // carregando em Lote
//    @KafkaListener(topics = "topic-1", groupId = "group_id", concurrency = "2")
//    public void listener(List<String> message) {
//        log.info("Thread: {}", Thread.currentThread().getId());
//        log.info("Received: {}", message);
//    }

    //ler particao especifica dentro do topic
    @KafkaListener(topicPartitions = {@TopicPartition(topic = "my-topic", partitions = "0-5")}, groupId = "my-group")
    public void listenerPartitionZerotoTwo(String message,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("Partition 0 - 2 -> {}", partition);
        log.info("Received: {}", message);
    }

    //ler particao especifica dentro do topic
    @KafkaListener(topicPartitions = {@TopicPartition(topic = "my-topic", partitions = "6-9")}, groupId = "my-group")
    public void listenerPartitionThreetoNine(String message,
                                  @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
        log.info("Partition 3 - 9 -> {}", partition);
        log.info("Received: {}", message);
    }

//    @KafkaListener(
//            topics = "topic-person",
//            groupId = "group_id",
//            containerFactory = "kafkaListenerContainerFactoryPerson"
//    )
//    @PersonCustomListener(groupId = "group_id")
//    public void listenerPerson(PersonDto personDto) {
//        log.info("Thread: {}", Thread.currentThread().getId());
//        log.info("Received name: {}", personDto.name());
//        log.info("Received age: {}", personDto.age());
//        log.info("Received Pessoa: {}", personDto);
//    }

//    @KafkaListener(
//            topics = "topic-person",
//            groupId = "group_id",
//            containerFactory = "kafkaListenerContainerFactoryPerson"
//    )
    @PersonCustomListener(topics = "topic-person.DLT", groupId = "group_id")
    public void personDLT(PersonDto personDto) {
        log.info("Received DLT: {}", personDto);
    }

//    @KafkaListener(
//            topics = "topic-person",
//            groupId = "group_id_01",
//            containerFactory = "kafkaListenerContainerFactoryPerson"
//    )
    @PersonCustomListener(groupId = "group_id_01")
    public void createPerson(PersonDto personDto) {
        log.info("Thread: {}", Thread.currentThread().getId());
//        log.info("Received create name: {}", personDto.name());
//        log.info("Received create age: {}", personDto.age());
//        log.info("Received create Pessoa: {}", personDto);

        // lança excecao customizada
        throw new IllegalArgumentException("tentou passar atritbuto diferente");
    }

    // processar em lote
//    @PersonCustomListener(groupId = "group_id_01")
//    public void createPersonList(List<PersonDto> listPersonDto) {
//        log.info("Thread: {}", Thread.currentThread().getId());
//        log.info("Received Lists create Pessoas: {}", listPersonDto);
//    }


    // processar em lote, pegando os headers, usando o type Message
//    @PersonCustomListener(groupId = "group_id_01")
//    public void createPersonList(List<Message<PersonDto>> messages) {
//        log.info("Thread: {}", Thread.currentThread().getId());
//        PersonDto personDto = messages.get(0).getPayload();
//        log.info("Messages -> {}", messages);
//        log.info("Pessoas -> {}", personDto);
//        log.info("Headers -> {}", messages.get(0).getHeaders());
//    }
}
