package com.thoughtworks.streams.actors.iot

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpecLike}

class ActorsTest(_system: ActorSystem)
    extends TestKit(_system)
    with Matchers
    with WordSpecLike
    with BeforeAndAfterAll
    with BeforeAndAfterEach {}
