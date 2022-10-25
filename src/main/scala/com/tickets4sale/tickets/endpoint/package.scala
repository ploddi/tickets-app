package com.tickets4sale.tickets

import com.tickets4sale.tickets.models.api.{InventoryResponse, OrderFailure, OrderRequest, OrderResponse, ResponseStatus}
import com.tickets4sale.tickets.services.Inventory
import zio._
import zio.json._
import zio.http._
import zio.http.model._


package object endpoint {

  val TicketsHttp: Http[Inventory, Throwable, Request, Response] = Http.collectZIO[Request] {
    case Method.GET -> !! / "inventory" / date(d) =>
      ZIO.serviceWithZIO[Inventory](_.findPerformances(d))
        .map(InventoryResponse.fromPerformanceList(_).toJson)
        .map(Response.json)

    case req@Method.POST -> !! / "inventory" / "order" =>

      for {
        body <- req.body.asString.map(_.fromJson[OrderRequest])
        inventory <- ZIO.service[Inventory]
        res <- body match {
          case Left(_) => ZIO.succeed(Response.status(Status.BadRequest))
          case Right(order) =>
            inventory.createOrder(order.performanceDate, order.show, order.tickets).either.map {
              case Right(performance) =>
                Response.json(
                  OrderResponse(
                    status = ResponseStatus.Success,
                    show = performance.show.title,
                    performanceDate = performance.performanceDate,
                    ticketsBought = order.tickets,
                    ticketsAvailable = performance.ticketsAvailable).toJson
                )
              case Left(error) =>
                Response.json(
                  OrderFailure(
                    status = ResponseStatus.Failure,
                    show = order.show,
                    performanceDate = order.performanceDate,
                    message = error.message).toJson
                )
            }
        }
      } yield res
  }

}
