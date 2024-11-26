package com.lnyynet.filecollect.task.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    
    @Bean
    fun downloadTasksExchange(): TopicExchange {
        return TopicExchange("download-tasks")
    }
    
    @Bean
    fun downloadTasksQueue(): Queue {
        return Queue("download-tasks.queue")
    }
    
    @Bean
    fun downloadTasksBinding(queue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(queue)
            .to(exchange)
            .with("download-task.created")
    }
    
    @Bean
    fun messageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }
} 