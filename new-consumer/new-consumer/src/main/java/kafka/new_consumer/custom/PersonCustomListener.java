package kafka.new_consumer.custom;

import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.annotation.KafkaListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@KafkaListener
public @interface PersonCustomListener {

    @AliasFor(annotation = KafkaListener.class, attribute = "groupId")
    String groupId() default "";

    @AliasFor(annotation = KafkaListener.class, attribute = "topics")
    String[] topics() default "topic-person";

    @AliasFor(annotation = KafkaListener.class, attribute = "containerFactory")
//    String containerFactory() default "jsonKafkaListenerContainerFactory";
    String containerFactory() default "kafkaListenerContainerFactoryPerson";

    @AliasFor(annotation = KafkaListener.class, attribute = "concurrency")
    String concurrency() default "2";

//    @AliasFor(annotation = KafkaListener.class, attribute = "errorHandler")
//    String errorHandler() default "myCustomHandler";
}
