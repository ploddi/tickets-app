package com.tickets4sale.tickets.models.api

import zio.json.JsonEncoder

sealed trait ResponseStatus

object ResponseStatus {
  final object Success extends ResponseStatus
  final object Failure extends ResponseStatus

  implicit val encoder: JsonEncoder[ResponseStatus] = JsonEncoder[String].contramap {
    case Success => "success"
    case Failure => "failure"
  }
}
