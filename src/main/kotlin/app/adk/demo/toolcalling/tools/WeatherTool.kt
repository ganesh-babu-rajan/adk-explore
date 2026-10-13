package app.adk.demo.toolcalling.tools

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.adk.tools.Annotations.Schema
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class WeatherTool {
    companion object {
        val httpClient = HttpClient.newHttpClient()
        val objectMapper = ObjectMapper()
        val cities = mapOf(
            "pune" to doubleArrayOf(18.52, 73.86),
            "mumbai" to doubleArrayOf(28.61, 77.21),
            "bangalore" to doubleArrayOf(12.98, 77.59),
            "goa" to doubleArrayOf(15.30, 74.12),
            "chennai" to doubleArrayOf(13.08, 80.27),
        )

        @JvmStatic
        @Schema(name = "getWeather", description = "Get the current temperature and wind spee ofor an Indian city. Supported cities: Pune, Mumbai, Delhi, Bangalore, Goa, Chennai")
        fun getWeather(
            @Schema(
                name = "city",
                description = "City name, for example Chennai"
            ) city: String?
        ): Map<String, Any?> {

            val cityKey = city?.trim()?.lowercase() ?: ""

            println("City: $cityKey & $cities")

            val coordinates = cities[cityKey] ?: return mapOf(
                "found" to false,
                "city" to city,
                "message" to "That city is not supported, try Pune, Chennai, Mumbai, Delhi, Bangalore or Goa"
            )

            val url = "http://api.open-meteo.com/v1/forecast?latitude=${coordinates[0]}&longitude=${coordinates[1]}&current=temperature_2m,wind_speed_10m"

            try {
                val request = HttpRequest.newBuilder()
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .uri(URI.create(url))
                    .build()

                val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

                if (response.statusCode() != 200) {
                    return mapOf(
                        "found" to false,
                        "city" to city,
                        "message" to "Weather service returned ${response.statusCode()}"
                    )
                }

                val current = objectMapper.readTree(response.body()).path("current")
                val result = mapOf(
                    "found" to true,
                    "city" to city,
                    "temperatureInC" to current.get("temperature_2m").asText(),
                    "windSpeedKmh" to current.get("wind_speed_10m").asText(),
                    "observedAt" to current.get("time").asText(),
                )

                return result

            } catch (e: Exception) {

                return mapOf(
                    "found" to false,
                    "city" to city,
                    "message" to "Could not reach Weather service ${e.message}"
                )

            }
        }
    }

}

