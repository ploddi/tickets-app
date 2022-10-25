package com.tickets4sale.tickets.models.api

import zio.json.{DeriveJsonEncoder, JsonEncoder, jsonField}

import java.time.LocalDate

case class OrderFailure(status: ResponseStatus,
                        show: String,
                        @jsonField("performance_date") performanceDate: LocalDate,
                        message: String)

object OrderFailure {

  implicit val encoder: JsonEncoder[OrderFailure] = DeriveJsonEncoder.gen[OrderFailure]

}