package io.billie.simulations.simplest_samples

import io.billie.utils.ConfigReader._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class FailedSimulation extends Simulation {
  private val url: String = readConfig().getConfig("restful_booker_settings")
                                .getString("url")

  private val httpConf: HttpProtocolBuilder = http.baseUrl(url)

  private val scn: ScenarioBuilder = scenario("RestfulBooker healthcheck simulation")
                                      .exec(http("Healthcheck request")
                                      .get("/ping")
                                      .check(status.is(404)))

  private val usersToBeInjected: Int = readConfig().getConfig("restful_booker_settings")
                                           .getInt("users_to_be_injected")

  setUp(scn.inject(atOnceUsers(usersToBeInjected))).protocols(httpConf)
                                                   .assertions(forAll.failedRequests.percent.lte(0))
}
