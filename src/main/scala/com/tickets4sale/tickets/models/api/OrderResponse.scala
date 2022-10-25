package com.tickets4sale.tickets.models.api

import zio.json.{DeriveJsonEncoder, JsonEncoder, jsonField}

import java.time.LocalDate

case class OrderResponse(status: ResponseStatus,
                         show: String,
                         @jsonField("performance_date") performanceDate: LocalDate,
                         @jsonField("tickets_bought") ticketsBought: Int,
                         @jsonField("tickets_available") ticketsAvailable: Int)

object OrderResponse {

  implicit val encoder: JsonEncoder[OrderResponse] = DeriveJsonEncoder.gen[OrderResponse]

}
