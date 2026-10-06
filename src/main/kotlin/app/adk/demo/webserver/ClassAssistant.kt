package app.adk.demo.webserver

import app.adk.demo.ModelFactory
import com.google.adk.agents.LlmAgent

class ClassAssistant {
    companion object {
        val ROOT_AGENT = LlmAgent.builder()
            .name("class-assistant")
            .description("Answers questions related to Agentic AI with JAVA and the ADK framework class")
            .instruction(
                """
                You are a class assistant for a weekend java training class on ADK.
                Answer at most 4 sentences to questions related to the class.
                If the question is about a code then give a code snippen from kotlin/java not the python one.
                If you are not sure about the answer, say "I don't know" instead of making up an answer.
                """.trimIndent()
            )
            .model(ModelFactory.genericLLM())
            .build()
    }
}