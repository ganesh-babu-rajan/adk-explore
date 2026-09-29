package app.adk.demo.config

import app.adk.demo.ModelFactory
import com.google.adk.agents.LlmAgent
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfig {
    val log = LoggerFactory.getLogger(BeanConfig::class.java)

    @Bean
    fun customerSupportAgent(): LlmAgent? {
        val model = ModelFactory.genericLLM()
        log.info(model.toString())

        return LlmAgent.builder()
            .name("customer-support-agent")
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
    }
}