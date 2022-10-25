package com.tickets4sale.tickets.models

import com.tickets4sale.tickets.models.csv.ShowRow

import java.time.LocalDate

case class Show(title: String,
                genre: Genre,
                openingDate: LocalDate,
                closingDate: LocalDate) {

  def isRunning(date: LocalDate): Boolean = {
    openingDate.isEqual(date) || closingDate.isEqual(date) ||
    (openingDate.isBefore(date) && closingDate.isAfter(date))
  }

}

object Show {
  val defaultShowRunningDays = 100

  def fromRow(row: ShowRow): Show = {
    Show(title = row.title,
      genre = row.genre,
      openingDate = row.openingDate,
      closingDate = row.openingDate.plusDays(defaultShowRunningDays - 1)
    )
  }

}


