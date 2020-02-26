package io.billie.utils

import com.typesafe.config.{Config, ConfigFactory}

object ConfigReader {

  def readConfig(): Config = {
    ConfigFactory.load("project.conf")
  }
}
