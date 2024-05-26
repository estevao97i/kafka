package kafka.new_consumer.dto;

import java.io.Serializable;

public record PersonDto(String name,
                        Integer age) implements Serializable {}
