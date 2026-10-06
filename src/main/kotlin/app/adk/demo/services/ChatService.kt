package app.adk.demo.services

import app.adk.demo.AgentFactory.Companion.customerSupportAgent
import app.adk.demo.ModelFactory
import app.adk.demo.support.Instructions
import com.google.adk.agents.LlmAgent
import com.google.adk.runner.InMemoryRunner
import com.google.genai.types.Content
import com.google.genai.types.Part
import org.jvnet.hk2.annotations.Service
import org.slf4j.LoggerFactory

@Service
class ChatService {
    val APP_NAME: String = "Customer-Support-Service"
    val log = LoggerFactory.getLogger(ChatService::class.java)

    fun ask(userId: String, question: String): String {
        // Runner and session must use the same app name key.
        val supportAgent = customerSupportAgent()

        //Runner is responsible for execute the agent and handle the input/output
        val runner = InMemoryRunner(supportAgent, APP_NAME)

        //Session
        val session = runner.sessionService()
            .createSession(APP_NAME, userId)
            .blockingGet()

        log.info("session Information: $session")

        //Run the agent with input and handle the output
        Content.fromParts(Part.fromText(question)).let { input ->
            runner.runAsync(session.userId(), session.id(), input)
                .blockingForEach { nextEvent ->
                    log.info("Next Event: ${nextEvent.stringifyContent()}")
                    if (nextEvent.finalResponse()) {
                        log.info("Final Response: ${nextEvent.stringifyContent()}")
                    }
                }
        }

        runner.close().blockingAwait()

        return ""
    }
}