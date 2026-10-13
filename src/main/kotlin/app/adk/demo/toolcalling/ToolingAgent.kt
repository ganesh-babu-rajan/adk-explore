package app.adk.demo.toolcalling

import app.adk.demo.ModelFactory
import app.adk.demo.toolcalling.tools.BookingTool
import app.adk.demo.toolcalling.tools.WeatherTool
import com.google.adk.agents.LlmAgent
import com.google.adk.events.Event
import com.google.adk.runner.InMemoryRunner
import com.google.adk.sessions.Session
import com.google.adk.tools.FunctionTool
import com.google.genai.types.Content
import com.google.genai.types.Part

class ToolingAgent {

    fun ask(runner: InMemoryRunner, session: Session, question: String) {
        println(" Question : $question")
        runner.runAsync(
            session.userId(), session.id(),
            Content.fromParts(Part.fromText(question))
        )
            .blockingForEach { event -> show(event) }
    }

    private fun show(event: Event) {
        event.functionCalls().forEach { call ->
            println(" [tool call] ${call.name().orElse("?")}")
        }

        event.functionResponses().forEach { response ->
            println(" [tool result] ${response.response().orElse(emptyMap())} ")
        }

        if (event.finalResponse()) {
            println(" [ answer ] ${event.stringifyContent().trim()}")
        }
    }
}

fun main() {

    val appName = "booking-tool-application"
    val userId = "TestUserId"

    val bookingToolAgent = ToolingAgent()
    //val question = "I want to cancel TL1001 , there are 6 hours before departure. How much will I get back?"
    //val question = "What is the status of booking TL1001 ?"
    //val question = "What is java google jdk ?"
    val question = "What is the weather in pune right now?"

    val bookingAgent = LlmAgent.builder()
        .name("booking-agent")
        .description("Answers question about flight bookings")
        .instruction(
            """
            You are a support agent for an airline
            Use getBooking to look up a booking before answering anything about it.
            Use getWeather when the passenger asks about conditions at a city.
            
            Use cancellationFee when the passenger asks about cancelling.
            
            Never invent a PNR, a fare or a status. If a tool says the booking was not found
            , tell the passenger that and ask them to check the code.
            
        """.trimIndent()
        )
        .tools(
            FunctionTool.create(BookingTool::class.java, "getBooking"),
            FunctionTool.create(BookingTool::class.java, "cancellationFee"),
            FunctionTool.create(WeatherTool::class.java, "getWeather"),
        )
        .model(ModelFactory.genericLLM())
        .build()

    val runner = InMemoryRunner(bookingAgent, appName)

    val session = runner.sessionService().createSession(appName, userId).blockingGet()

    bookingToolAgent.ask(runner, session, question )
}
