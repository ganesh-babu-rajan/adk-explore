package app.adk.demo.workflow

import app.adk.demo.ModelFactory
import com.google.adk.agents.LlmAgent
import com.google.adk.agents.SequentialAgent

class SequentialAgents {

    //3 Agents

    fun run() {
        // Implement the logic for sequential agents workflow here
    }

}

fun main() {
    val sequentialAgents = SequentialAgents()
    println("Sequential Agents workflow initialized: $sequentialAgents")

    val model = ModelFactory.genericLLM().also { model ->
        println("Model initialized: $model")
    }

    val extractorAgent =  LlmAgent.builder()
        .name("extractor-agent")
        .description("Pulls the fact about passenger information.")
        .instruction(
            """
                Read the passenger's message and extract only these fields.
                Use exactly this format, one per line, nothing else:
                Name: <name>
                Age: <age>
                PNR: <pnr>
                Issue: <issue>
                HoursBeforeDeparture: <hours before departure>
            """.trimIndent()
        )
        .model(model)
        .outputKey("compliant")
        .build()

    val policyAgent =  LlmAgent.builder()
        .name("extractor-agent")
        .description("Pulls the fact about passenger information.")
        .instruction(
            """
               Apply this policy to the extracted information and provide a response:
           
            """.trimIndent()
        )
        .model(model)
        .outputKey("decision")
        .build()

    val writerAgent =  LlmAgent.builder()
        .name("extractor-agent")
        .description("Pulls the fact about passenger information.")
        .instruction(
            """
               Write a response to the customer based on the decision from the policy agent.
               
           
            """.trimIndent()
        )
        .model(model)
        .outputKey("reply")
        .build()

    val compliantworkflowagent = SequentialAgent.builder()
        .name("compliant agents workflow")
        .description("A sequential agent that runs multiple agents in order. extractor -> policy -> writer")
        .subAgents(listOf(extractorAgent, policyAgent, writerAgent))
        .build()


}