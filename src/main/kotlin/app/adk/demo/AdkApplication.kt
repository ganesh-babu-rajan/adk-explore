package app.adk.demo

import com.google.adk.agents.LlmAgent
import com.google.adk.runner.InMemoryRunner
import com.google.genai.types.Content
import com.google.genai.types.Part
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger(DemoApplication::class.java)
    runApplication<DemoApplication>(*args)

    //OLLAMA_BASE_URL=http://localhost:1234/api/v1;
    //OPENAI_API_KEY=sk-lm-KXn9hNcr:4kQu9wXynezd9IWWDRkx

    val model = ModelFactory.genericLLM()
    log.info(model.toString())

    //Create Agent
    val greeterAgent = LlmAgent.builder()
        .name("greeter")
        .description("A simple agent that greets the user.")
        .instruction(
            """
            You are a helpful assistant that greets the user. 
            Answer in at most threee sentences
            If you do not know something, say sa instead of guessing.
        """.trimIndent()
        )
        .model(model)
        .build()

    // Runner and session must use the same app name key.
    val appName = "greeter-application"
    val userId = "TestUserId"

    //Runner is responsible for execute the agent and handle the input/output
    val runner = InMemoryRunner(greeterAgent, appName)

    //Session
    val session = runner.sessionService()
        .createSession(appName, userId)
        .blockingGet()

    log.info("session Information: $session")

    //Run the agent with input and handle the output
    Content.fromParts(Part.fromText("Explain the difference between a cat and a dog.")).let { input ->
        runner.runAsync(session.userId(), session.id(), input)
            .blockingForEach { nextEvent ->
                log.info("Next Event: ${nextEvent.stringifyContent()}")
                if (nextEvent.finalResponse()) {
                    log.info("Final Response: ${nextEvent.stringifyContent()}")
                }
            }
    }
    runner.close().blockingAwait()
}
