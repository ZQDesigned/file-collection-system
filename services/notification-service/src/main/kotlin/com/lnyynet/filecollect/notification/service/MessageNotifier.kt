package com.lnyynet.filecollect.notification.service

import com.lnyynet.filecollect.notification.model.entity.Message
import com.lnyynet.filecollect.notification.model.entity.MessageType
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class MessageNotifier(
    private val messagingTemplate: SimpMessagingTemplate,
    private val subscriptionService: SubscriptionService
) {
    
    fun notifyNewMessage(message: Message) {
        if (subscriptionService.shouldNotify(message.userId, message.type)) {
            messagingTemplate.convertAndSendToUser(
                message.userId,
                "/queue/messages",
                MessageEvent(
                    type = "NEW_MESSAGE",
                    data = MessageEventData(
                        id = message.id!!,
                        title = message.title,
                        content = message.content,
                        messageType = message.type
                    )
                )
            )
        }
    }
}

data class MessageEvent(
    val type: String,
    val data: MessageEventData
)

data class MessageEventData(
    val id: Long,
    val title: String,
    val content: String,
    val messageType: MessageType
) 