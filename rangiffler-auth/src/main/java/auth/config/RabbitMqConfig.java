package auth.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Autowired
    public RabbitMqConfig() {}

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("deadLetterExchange");
    }

    @Bean
    public Queue errorQueue() {
        return QueueBuilder.durable("q.userdata-registration.err").build();
    }

    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable("q.userdata-registration")
                .withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetter")
                .build();
    }

    @Bean
    public Binding errorQueueBinding() {
        return BindingBuilder.bind(errorQueue()).to(deadLetterExchange()).with("deadLetter");
    }

}
