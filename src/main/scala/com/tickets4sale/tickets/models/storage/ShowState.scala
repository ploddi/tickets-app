package com.tickets4sale.tickets.models.storage

import com.tickets4sale.tickets.models.{Show, Performance}

import java.time.LocalDate

case class ShowState(show: Show, ticketsByDate: Map[LocalDate, Int]) {

  def toPerformance(performanceDate: LocalDate): Performance =
    Performance(
      show,
      performanceDate,
      ticketsByDate.getOrElse(performanceDate, Performance.defaultTicketsPerPerformance))

  def makeOrder(performanceDate: LocalDate, tickets: Int): Either[String, ShowState] = {
    val ticketsAvailable =
      ticketsByDate.getOrElse(performanceDate, Performance.defaultTicketsPerPerformance)

    if (ticketsAvailable < tickets) {
      Left("Not enough tickets")
    } else {
      Right(copy(ticketsByDate = ticketsByDate + (performanceDate -> (ticketsAvailable - tickets))))
    }
  }

}