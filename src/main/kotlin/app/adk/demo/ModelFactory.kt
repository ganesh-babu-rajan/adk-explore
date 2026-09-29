package app.adk.demo

import com.google.adk.models.ApigeeLlm
import com.google.adk.models.BaseLlm

class ModelFactory {


    companion object {

        fun genericLLM(): BaseLlm {
            log.info("OLLAMA_BASE_URL : " + System.getenv("OLLAMA_BASE_URL"))

            log.info("OPEN_API_KEY: " + System.getenv("OPENAI_API_KEY"))

            return ApigeeLlm.builder().modelName("apigee/openai/gpt-oss-20b")
                .proxyUrl("http://localhost:1234/v1")
                .apiType(ApigeeLlm.ApiType.CHAT_COMPLETIONS)
                .build()
        }
    }
}