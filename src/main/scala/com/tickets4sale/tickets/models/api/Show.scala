package com.tickets4sale.tickets.models.api

import com.tickets4sale.tickets.models.Performance
import zio.json._

case class Show(title: String,
                @jsonField("tickets_available") ticketsAvailable: Int,
                price: BigDecimal)

object Show {

  def fromPerformance(performance: Performance): Show = Show(
    title = performance.show.title,
    price = performance.ticketPrice,
    ticketsAvailable = performance.ticketsAvailable
  )

  implicit val encoder: JsonEncoder[Show] = DeriveJsonEncoder.gen[Show]

}