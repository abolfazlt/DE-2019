package json

import java.net._
import java.io._

import json.Server.logger

import scala.io._
import org.apache.log4j.Logger


case class Satrap(name: String, tax: Map[String, Int])

object Server extends App {
  private val logger = Logger.getLogger("Json Sever")
  new Server start
}

class Server {
  val server = new ServerSocket(9999)
  logger.info("Server initialized")

  def start(): Unit = {
    logger.info("Server started.")
    while (true) {
      val s = server.accept()
      val in = new BufferedSource(s.getInputStream()).getLines()
      val out = new PrintStream(s.getOutputStream())
      val line = in.next()
      val satrap = JsonUtil.fromJson[Satrap](line)
      out.println("Hello " + satrap.name)
      logger.info("Server received from satrap: " + satrap.name)
      out.flush()
      s.close()
    }
  }
}


