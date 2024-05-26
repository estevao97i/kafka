package kafka.new_producer.dto;

import java.io.Serializable;

public record PersonDto(
        String name,
        Integer age) implements Serializable {
}
