package protobuf

import org.apache.log4j.Logger
import io.grpc.{Server, ServerBuilder}
import protocols.Satrap._

import scala.concurrent.{ExecutionContext, Future}

object ProtoServer {
  private val logger = Logger.getLogger("Protobuf Sever")
  private val port = 9999

  def main(args: Array[String]): Unit = {
    val server = new ProtoServer(ExecutionContext.global)
    server.start()
    server.blockUntilShutdown()
  }

}

class ProtoServer(executionContext: ExecutionContext) {
  self =>
  private[this] var server: Server = null

  private def start(): Unit = {
    server = ServerBuilder.
      forPort(ProtoServer.port).
      addService(SatrapServerGrpc.bindService(
        new SatrapServerImp(), executionContext)).
      build.start
    ProtoServer.logger.info("Server started, listening on " + ProtoServer.port)
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        ProtoServer.logger.info("*** shutting down gRPC server since JVM is shutting down")
        self.stop()
        ProtoServer.logger.error("*** server shut down")
      }
    })
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private def blockUntilShutdown(): Unit = {
    if (server != null) {
      server.awaitTermination()
    }
  }

  private class SatrapServerImp extends SatrapServerGrpc.SatrapServer {
    override def sendSatrap(request: SatrapRequest) = {
      val originRequest = SatrapRequest.toByteArray(request)
      println(originRequest.mkString(" "))
      println(SatrapRequest.parseFrom(originRequest))
      val reply = GenericReply(request.name)
      Future.successful(reply)
    }

  }

}
