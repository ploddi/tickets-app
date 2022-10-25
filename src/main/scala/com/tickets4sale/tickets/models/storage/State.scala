package com.tickets4sale.tickets.models.storage

import com.tickets4sale.tickets.models.{Show, Performance}

import java.time.LocalDate

case class State(shows: Map[String, ShowState]) {

  def findPerformances(performanceDate: LocalDate): List[Performance] = {
    shows.values
      .filter(_.show.isRunning(performanceDate))
      .map(_.toPerformance(performanceDate))
      .toList
  }

  def findPerformance(performanceDate: LocalDate, title: String): Option[Performance] = {
    shows.get(title).map(_.toPerformance(performanceDate))
  }

  def createOrder(performanceDate: LocalDate, title: String, tickets: Int): Either[String, State] = {
    for {
      state <- shows.get(title).toRight("Show not found")
      updatedState <- state.makeOrder(performanceDate, tickets)
    } yield copy(shows = shows + (title -> updatedState))
  }

}

object State {

  def fromShowsList(shows: List[Show]): State = {
    State(shows.map(show => show.title -> ShowState(show, Map.empty)).toMap)
  }

}