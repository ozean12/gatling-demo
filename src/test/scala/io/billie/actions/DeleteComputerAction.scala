package io.billie.actions

import io.billie.utils.ConfigReader._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

object DeleteComputerAction {
  private val url: String = readConfig().getConfig("computer_database_settings")
                                        .getString("url")

  private val headers = Map("Content-Type" -> "application/x-www-form-urlencoded")

  private val request = http("Request: Delete a recently created computer")
                          .post(session => {
                            url + "/" + session("computer_id").as[String] + "/delete"
                          })
                          .headers(headers)
                          .check(status.is(200))

  def execute(): ScenarioBuilder = {
    scenario("Action: Delete a recently created computer").exec(request)
  }
}
