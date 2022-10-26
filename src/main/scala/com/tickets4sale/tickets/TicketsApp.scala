package com.tickets4sale.tickets

import com.tickets4sale.tickets.services.{Inventory, InventoryLoader}
import com.tickets4sale.tickets.endpoint.TicketsHttp
import zio._
import zio.http.ServerConfig.LeakDetectionLevel
import zio.http._

object TicketsApp extends ZIOAppDefault {
  val PORT = 8080

  val config: ServerConfig = ServerConfig.default
    .port(PORT)
    .leakDetection(LeakDetectionLevel.PARANOID)

  override def run: ZIO[Any, Throwable, Nothing] = {
    Server.serve(TicketsHttp.provideLayer(Inventory.fromCsvResource("/shows.csv")))
      .provide(ServerConfig.live(config), Server.live, InventoryLoader.live)
  }
}
