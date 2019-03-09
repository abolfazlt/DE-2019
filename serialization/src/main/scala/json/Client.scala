package json

import java.net._
import java.io._

import MarshallableImplicits._

import scala.io._

object Client extends App {
  val client = new Client
  client.sendMessage(client.createJson())

//  var counter = 0
//  val start = System.currentTimeMillis()
//  while (counter < 100000) {
//    client.sendMessage(client.createJson())
//    counter += 1
//  }
//  val end = System.currentTimeMillis()
//  println(end - start)
}

class Client {

  def sendMessage(msg: String): Unit = {
    val s = new Socket(InetAddress.getByName("localhost"), 9999)
    lazy val in = new BufferedSource(s.getInputStream()).getLines()
    val out = new PrintStream(s.getOutputStream())

    out.println(msg)
    out.flush()
    println("Received: " + in.next())

    s.close()
  }

  def createJson() = {
    val satrap = Satrap("first", Map("worker" -> 10))
    satrap.toJson
  }
}