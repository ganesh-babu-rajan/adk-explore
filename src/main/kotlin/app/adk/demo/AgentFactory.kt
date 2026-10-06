package app.adk.demo

import app.adk.demo.support.Instructions
import com.google.adk.agents.LlmAgent

class AgentFactory {
    companion object {
        fun customerSupportAgent(): LlmAgent? {
            val model = ModelFactory.genericLLM()
            log.info(model.toString())

            return LlmAgent.builder()
                .name("customer-support-agent")
                .description("Handles customer support queries and provides helpful responses.")
                .instruction(Instructions().GOOD_INSTRUCTIONS.trimIndent())
                .model(model)
                .build()
        }
    }
}