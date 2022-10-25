package com.tickets4sale.tickets.services

import com.tickets4sale.tickets.models.Show
import com.tickets4sale.tickets.models.csv.ShowRow
import kantan.csv._
import kantan.csv.ops._
import zio.{ZIO, ZLayer}

trait InventoryLoader {

  def loadShows(resourcePath: String): ZIO[Any, Throwable, List[Show]]

}

class InventoryLoaderImpl extends InventoryLoader {

  override def loadShows(resourcePath: String): ZIO[Any, Throwable, List[Show]] = {
    ZIO
      .attemptBlocking(getClass.getResource(resourcePath).unsafeReadCsv[List, ShowRow](rfc))
      .map(_.map(Show.fromRow))
  }
}

object InventoryLoader {

  def live =
    ZLayer.succeed(new InventoryLoaderImpl)

}