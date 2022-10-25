package com.tickets4sale.tickets.models.api

import zio.json._

case class InventoryGenre(genre: String, shows: List[Show])

object InventoryGenre {

  implicit val encoder: JsonEncoder[InventoryGenre] = DeriveJsonEncoder.gen[InventoryGenre]

}
