package protobuf

import java.util.concurrent.TimeUnit
import java.util.logging.{Level, Logger}

import protocols.Satrap._
import io.grpc.{ManagedChannel, ManagedChannelBuilder, StatusRuntimeException}
import protocols.Satrap.SatrapServerGrpc.SatrapServerBlockingStub

case class Satrap(name: String, tax: Map[String, Int])

object ProtoClient {
  def apply(host: String, port: Int): ProtoClient = {
    val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build
    val blockingStub = SatrapServerGrpc.blockingStub(channel)
    new ProtoClient(channel, blockingStub)
  }

  def main(args: Array[String]): Unit = {
    val client = ProtoClient("localhost", 9999)
    try {
      val satrap = Satrap("first", Map("worker" -> 10))
      client.send(satrap)
//      var counter = 0
//      val start = System.currentTimeMillis()
//      while (counter < 100000) {
//        client.send(satrap)
//        counter += 1
//      }
//      val end = System.currentTimeMillis()
//      println(end - start)
    } finally {
      client.shutdown()
    }
  }
}

class ProtoClient private(
                                private val channel: ManagedChannel,
                                private val blockingStub: SatrapServerBlockingStub
                              ) {
  private[this] val logger = Logger.getLogger(classOf[ProtoClient].getName)

  def shutdown(): Unit = {
    channel.shutdown.awaitTermination(5, TimeUnit.SECONDS)
  }

  def send(satrap: Satrap): Unit = {
    logger.info("Will try to send " + satrap.name + " ...")
    val request = SatrapRequest(name = satrap.name, taxes = satrap.tax.map(
      t => Tax(t._1, t._2)
    ).toSeq)
    try {
      val response = blockingStub.sendSatrap(request)
      logger.info("send message: " + response.message)
    }
    catch {
      case e: StatusRuntimeException =>
        logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus)
    }
  }
}