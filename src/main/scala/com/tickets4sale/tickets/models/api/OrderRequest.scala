package com.tickets4sale.tickets.models.api

import zio.json.{DeriveJsonDecoder, JsonDecoder, jsonField}

import java.time.LocalDate

case class OrderRequest(show: String,
                        @jsonField("performance_date") performanceDate: LocalDate,
                        tickets: Int)

object OrderRequest {

  implicit val decoder: JsonDecoder[OrderRequest] = DeriveJsonDecoder.gen[OrderRequest]

}
