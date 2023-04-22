package auth.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqHelper {

    private final AmqpAdmin admin;

    @Autowired
    public RabbitMqHelper(AmqpAdmin admin) {
        this.admin = admin;
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        DirectExchange exchange = new DirectExchange("deadLetterExchange");
        admin.declareExchange(exchange);
        return exchange;
    }

    @Bean
    public Queue errorQueue() {
        Queue queue = QueueBuilder.durable("q.userdata-registration.err").build();
        admin.declareQueue(queue);
        return queue;
    }

    @Bean
    public Queue mainQueue() {
        Queue queue = QueueBuilder.durable("q.userdata-registration")
                .withArgument("x-dead-letter-exchange", "deadLetterExchange")
                .withArgument("x-dead-letter-routing-key", "deadLetter")
                .build();
        admin.declareQueue(queue);
        return queue;
    }

    @Bean
    public Binding errorQueueBinding() {
        Binding binding = BindingBuilder.bind(errorQueue())
                .to(deadLetterExchange())
                .with("deadLetter");
        admin.declareBinding(binding);
        return binding;
    }

}
