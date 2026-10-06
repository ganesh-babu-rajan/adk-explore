package app.adk.demo.webserver

import com.google.adk.web.AdkWebServer

class WebAdkServer {

}

fun main() {
    println("Starting ADK Web Server...")
    println("ADK Web Server is running on http://localhost:8080/dev-ui (development only)")
    AdkWebServer.start(ClassAssistant.ROOT_AGENT)
}