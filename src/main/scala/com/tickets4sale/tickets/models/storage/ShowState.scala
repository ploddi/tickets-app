package com.tickets4sale.tickets.models.storage

import com.tickets4sale.tickets.models.{Show, Performance}

import java.time.LocalDate

case class ShowState(show: Show, ticketsByDate: Map[LocalDate, Int]) {

  def toPerformance(performanceDate: LocalDate): Performance =
    Performance(
      show,
      performanceDate,
      ticketsAvailable(performanceDate))

  def makeOrder(performanceDate: LocalDate, tickets: Int): Either[String, ShowState] = {
    val available = ticketsAvailable(performanceDate)

    if (show.isRunning(performanceDate)) {
      if (available < tickets) {
        Left("Not enough tickets")
      } else {
        Right(copy(ticketsByDate = ticketsByDate + (performanceDate -> (available - tickets))))
      }
    } else {
      Left("Show is not running")
    }
  }

  private def ticketsAvailable(performanceDate: LocalDate): Int =
    ticketsByDate.getOrElse(performanceDate, Performance.defaultTicketsPerPerformance)

}