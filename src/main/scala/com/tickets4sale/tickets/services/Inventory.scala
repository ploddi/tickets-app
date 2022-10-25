package com.tickets4sale.tickets.services

import com.tickets4sale.tickets.models.Performance
import com.tickets4sale.tickets.models.storage.State
import zio.{Ref, ZIO, ZLayer}

import java.time.LocalDate

trait Inventory {

  def findPerformances(date: LocalDate): ZIO[Any, Nothing, List[Performance]]

  def createOrder(performanceDate: LocalDate, show: String, tickets: Int): ZIO[Any, InventoryError, Performance]

}

class InMemoryInventory(state: Ref[State]) extends Inventory {
  override def findPerformances(date: LocalDate): ZIO[Any, Nothing, List[Performance]] = {
    state.get.map(_.findPerformances(date))
  }

  override def createOrder(performanceDate: LocalDate, title: String, tickets: Int): ZIO[Any, InventoryError, Performance] = {
    state.modify { currentState =>
      (for {
        updatedState <- currentState.makeOrder(performanceDate, title, tickets)
        performance <- updatedState.findPerformance(performanceDate, title).toRight("Performance not found")
      } yield (updatedState, performance)) match {
        case Left(error) => (Left(InventoryError(error)), currentState)
        case Right((updatedState, performance)) => (Right(performance), updatedState)
      }
    }.absolve
  }
}


object Inventory {

  def fromCsvResource(resource: String): ZLayer[InventoryLoader, Throwable, Inventory] = ZLayer {
    for {
      loader <- ZIO.service[InventoryLoader]
      shows <- loader.loadShows(resource)
      ref <- Ref.make(State.fromShowsList(shows))
    } yield new InMemoryInventory(ref)
  }
}