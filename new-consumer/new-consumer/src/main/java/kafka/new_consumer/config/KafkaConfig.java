package kafka.new_consumer.config;

import kafka.new_consumer.dto.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.converter.BatchMessageConverter;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Configuration
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());

        // para carregar em lote
//        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PersonDto> consumerFactoryObjPerson() {
        var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        var jsonDeserializer = new JsonDeserializer<>(PersonDto.class)
                .trustedPackages("*")
                .forKeys();

        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PersonDto> kafkaListenerContainerFactoryPerson() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, PersonDto>();
        factory.setConsumerFactory(consumerFactoryObjPerson());
        factory.setCommonErrorHandler(defaultErrorHandler());
//        factory.setRecordInterceptor(interceptorKafka());
        return factory;
    }

    public ProducerFactory<String, PersonDto> pf() {
        var configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>());
    }

    private DefaultErrorHandler defaultErrorHandler() {
        // passar para outro topic se quebrar
        var recoverer = new DeadLetterPublishingRecoverer(new KafkaTemplate<>(pf()));
        // se quebrar vai tentar mais 2 vezes
        return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2));
    }

    private RecordInterceptor<String, PersonDto> interceptorKafka() {
        return new RecordInterceptor<String, PersonDto>() {
            @Override
            public ConsumerRecord<String, PersonDto> intercept(ConsumerRecord<String, PersonDto> record, Consumer<String, PersonDto> consumer) {
                return record;
            }

            @Override
            public void success(ConsumerRecord<String, PersonDto> record, Consumer<String, PersonDto> consumer) {
                log.info("Success");
                RecordInterceptor.super.success(record, consumer);
            }

            @Override
            public void failure(ConsumerRecord<String, PersonDto> record, Exception exception, Consumer<String, PersonDto> consumer) {
                log.info("Failure");
                RecordInterceptor.super.failure(record, exception, consumer);
            }
        };

    }

//    private RecordInterceptor<String, PersonDto> adultInterceptor() {
//        return (record, consumer) -> {
//            log.info("record => {}", record);
//            PersonDto personDto = record.value();
//            return Objects.equals(personDto.name(), "teste") ? null : record;
//        };
//    }

    @Bean
    public ConsumerFactory jsonConsumerFactory() {
        var configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory jsonKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(jsonConsumerFactory());
        factory.setRecordMessageConverter(new JsonMessageConverter());

        // processar em Batch (lote)
//        factory.setBatchMessageConverter(new BatchMessagingMessageConverter(new JsonMessageConverter()));
//        factory.setBatchListener(true);
        return factory;
    }

}
