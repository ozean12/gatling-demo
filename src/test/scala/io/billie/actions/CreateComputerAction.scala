package io.billie.actions

import java.time.LocalDate
import java.time.temporal.ChronoUnit

import io.billie.utils.ConfigReader._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder
import org.apache.commons.lang.RandomStringUtils

import scala.util.Random

object CreateComputerAction {
  private val url: String = readConfig().getConfig("computer_database_settings")
                                        .getString("url")

  private val headers = Map("Content-Type" -> "application/x-www-form-urlencoded")

  private val feeder = Iterator.continually(Map("name" -> RandomStringUtils.randomAlphanumeric(30).toUpperCase(),
                                                "introduced" -> LocalDate.now.minus(Random.nextInt(365),
                                                                                    ChronoUnit.DAYS),
                                                "discontinued" -> LocalDate.now.plus(Random.nextInt(365),
                                                                                     ChronoUnit.DAYS),
                                                "company" -> String.valueOf(1 + Random.nextInt(42))))

  private val request: HttpRequestBuilder = http("Request: Create a new computer")
                                              .post(url)
                                              .headers(headers)
                                              .formParam("name", "${name}")
                                              .formParam("introduced", "${introduced}")
                                              .formParam("discontinued", "${discontinued}")
                                              .formParam("company", "${company}")
                                              .check(status.is(200))

  def execute(): ScenarioBuilder = {
    scenario("Action: Create a new computer").feed(feeder)
                                                           .exec(request)
  }
}
