package com.lnyynet.filecollect.notification.config

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    
    @Bean
    fun emailExchange(): DirectExchange {
        return DirectExchange("email.exchange")
    }
    
    @Bean
    fun emailQueue(): Queue {
        return QueueBuilder.durable("email.send.queue")
            .withArgument("x-dead-letter-exchange", "email.dlx")
            .withArgument("x-dead-letter-routing-key", "email.dlq")
            .build()
    }
    
    @Bean
    fun emailBinding(): Binding {
        return BindingBuilder
            .bind(emailQueue())
            .to(emailExchange())
            .with("email.send")
    }
    
    @Bean
    fun deadLetterExchange(): DirectExchange {
        return DirectExchange("email.dlx")
    }
    
    @Bean
    fun deadLetterQueue(): Queue {
        return QueueBuilder.durable("email.dlq").build()
    }
    
    @Bean
    fun deadLetterBinding(): Binding {
        return BindingBuilder
            .bind(deadLetterQueue())
            .to(deadLetterExchange())
            .with("email.dlq")
    }
} 