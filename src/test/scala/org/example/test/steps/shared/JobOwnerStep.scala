package org.example.test.steps.shared

import io.cucumber.scala.{EN, ScalaDsl}
import org.scalatest.funsuite.AnyFunSuite

class JobOwnerStep extends AnyFunSuite with ScalaDsl with EN  {

  Given("""I'm the product owner of financial services""") { () =>
    // Write code here that turns the phrase above into concrete actions
    println("OK")
  }
}
