package kafka.new_producer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Configuration
public class KafkaConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

//    @Bean
//    public RoutingKafkaTemplate kafkaRoutingTemplate(GenericApplicationContext context,
//                                                     ProducerFactory producerFactory) {
//
//        var producerFactoryObject = producerFactoryObject();
//        context.registerBean(DefaultKafkaProducerFactory.class,"jsonPF", producerFactoryObject);
//
//        Map<Pattern, ProducerFactory<Object, Object>> map = new LinkedHashMap<>();
//        map.put(Pattern.compile("topic-1"), producerFactory);
//        map.put(Pattern.compile("topic-person"), producerFactoryObject);
//        return new RoutingKafkaTemplate(map);
//    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        var configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, Serializable> kafkaTemplateObject() {
        return new KafkaTemplate(producerFactoryObject());
    }

    @Bean
    public ProducerFactory producerFactoryObject() {
        var configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>());
    }

    // Para criar tópicos dentro do projeto (setando na mão)
    @Bean
    public KafkaAdmin kafkaAdmin() {
        var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaAdmin.NewTopics topics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder.name("topic-1").partitions(2).build(),
                TopicBuilder.name("topic-person").partitions(2).build(),
                TopicBuilder.name("topic-person.DLT").partitions(2).build(),
                TopicBuilder.name("my-topic").partitions(10).build()
        );
    }

//    @Bean
//    public NewTopic topic1() {
//        return new NewTopic("topic_01", 10, Short.parseShort("1"));
//    }
}
