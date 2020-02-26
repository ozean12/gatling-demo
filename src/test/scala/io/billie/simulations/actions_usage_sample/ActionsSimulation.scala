package io.billie.simulations.actions_usage_sample

import io.billie.actions.{CreateComputerAction, DeleteComputerAction, FindComputerAction}
import io.gatling.core.Predef.Simulation
import io.billie.utils.ConfigReader._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration._

class ActionsSimulation extends Simulation {
  private val usersToBeInjected: Int = readConfig().getConfig("computer_database_settings")
                                                   .getInt("users_to_be_injected")

  val fullScenario: ScenarioBuilder = CreateComputerAction.execute()
                                                          .exec(FindComputerAction.execute())
                                                          .exec(DeleteComputerAction.execute())

  setUp(fullScenario.inject(rampUsers(usersToBeInjected) during (5 seconds)).protocols(http))
}
