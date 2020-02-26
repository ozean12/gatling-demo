package io.billie.actions

import io.billie.utils.ConfigReader._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

object FindComputerAction {
  private val url: String = readConfig().getConfig("computer_database_settings")
                                        .getString("url")

  private val request = http("Request: Find a recently created computer")
                          .get(session => {
                            url + "?f=" + session("name").as[String]
                          })
                          .check(status.is(200))
                          .check(css("table.computers tbody tr").count.is(1))
                          .check(css("table.computers tbody tr td:first-child")
                                   .transform((s: String) => s.eq("${name}")))
                          .check(css("table.computers tbody tr td:first-child a", "href")
                                   .transform((s: String) => s.split("/").last)
                                   .saveAs("computer_id"))

  def execute(): ScenarioBuilder = {
    scenario("Action: Find a recently created computer").exec(request)
  }
}
