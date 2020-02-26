package io.billie.simulations.feeders_usage_samples

import java.time.LocalDate
import java.time.temporal.ChronoUnit

import io.gatling.core.Predef.Simulation
import io.billie.utils.ConfigReader._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.util.Random

class CustomFeederSimulation extends Simulation {

  private val url: String = readConfig().getConfig("computer_database_settings")
                                        .getString("url")

  private val httpConf: HttpProtocolBuilder = http.baseUrl(url)

  private val headers = Map("Content-Type" -> "application/x-www-form-urlencoded")

  private val feeder = Iterator.continually(Map("name" -> Random.nextString(20).toUpperCase,
                                                "introduced" -> LocalDate.now.minus(Random.nextInt(365),
                                                                                   ChronoUnit.DAYS),
                                                "discontinued" -> LocalDate.now.plus(Random.nextInt(365),
                                                                                     ChronoUnit.DAYS),
                                                "company" -> String.valueOf(1 + Random.nextInt(42))))

  private val request = http("Request: Create a new computer")
                          .post(url)
                          .headers(headers)
                          .formParam("name", "${name}")
                          .formParam("introduced", "${introduced}")
                          .formParam("discontinued", "${discontinued}")
                          .formParam("company", "${company}")
                          .check(status.is(200))

  private val scn: ScenarioBuilder = scenario("Scenario: Create a new computer").feed(feeder)
                                                                                .exec(request)

  private val usersToBeInjected: Int = readConfig().getConfig("computer_database_settings")
                                                   .getInt("users_to_be_injected")

  setUp(scn.inject(rampUsers(usersToBeInjected) during (5 seconds)).protocols(httpConf))
}
