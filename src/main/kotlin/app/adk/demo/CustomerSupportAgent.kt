package app.adk.demo

import app.adk.demo.services.ChatService

fun main() {
    val chatService = ChatService()
    val response = chatService.ask("userId","A customer says their order has not arrived. What should I do?")
    println(response)
}