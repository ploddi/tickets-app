package com.tickets4sale.tickets.models

import java.time.LocalDate

case class Performance(show: Show,
                       performanceDate: LocalDate,
                       ticketsAvailable: Int) {

  def ticketPrice: BigDecimal = {
    val isDiscountApplicable =
      show.openingDate.plusDays(Performance.defaultDiscountDelay - 1).isBefore(performanceDate)

    if (isDiscountApplicable) {
      Performance.defaultPriceForGenre(show.genre) * Performance.defaultDiscountFactor
    } else {
      Performance.defaultPriceForGenre(show.genre)
    }
  }

}

object Performance {

  val defaultDiscountDelay = 80
  val defaultDiscountFactor = 0.8
  val defaultTicketsPerPerformance = 100

  def defaultPriceForGenre(genre: Genre): BigDecimal = genre match {
    case Genre.Musicals => BigDecimal.decimal(70.0)
    case Genre.Comedy => BigDecimal.decimal(50.0)
    case Genre.Drama => BigDecimal.decimal(40.0)
  }


}