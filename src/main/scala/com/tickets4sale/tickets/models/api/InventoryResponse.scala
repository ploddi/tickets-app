package com.tickets4sale.tickets.models.api

import com.tickets4sale.tickets.models.Performance
import zio.json._

case class InventoryResponse(inventory: List[InventoryGenre])

object InventoryResponse {

  implicit val encoder: JsonEncoder[InventoryResponse] = DeriveJsonEncoder.gen[InventoryResponse]

  def fromPerformanceList(performances: List[Performance]): InventoryResponse =
    InventoryResponse(
      performances
        .groupBy(_.show.genre.value)
        .map {
          case (genre, list) => InventoryGenre(genre, list.map(Show.fromPerformance))
        }
        .toList
    )

}
