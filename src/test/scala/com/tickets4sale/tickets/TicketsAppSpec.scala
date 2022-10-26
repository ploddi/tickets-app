package com.tickets4sale.tickets

import com.tickets4sale.tickets.endpoint.TicketsHttp
import com.tickets4sale.tickets.models.storage.State
import com.tickets4sale.tickets.models.{Genre, Show}
import com.tickets4sale.tickets.services.Inventory
import zio.http._
import zio.test._
import zio.test.junit.JUnitRunnableSpec

import java.time.LocalDate

class TicketsAppSpec extends JUnitRunnableSpec {

  val stateFixture = State.fromShowsList(List(
    Show("Show 1", Genre.Drama, LocalDate.parse("2022-01-01"), LocalDate.parse("2022-01-01").plusDays(99)),
    Show("Show 2", Genre.Drama, LocalDate.parse("2022-05-01"), LocalDate.parse("2022-05-01").plusDays(99)),
    Show("Show 3", Genre.Drama, LocalDate.parse("2022-10-01"), LocalDate.parse("2022-10-01").plusDays(99)),
  ))

  val show2 = "{\"inventory\":[{\"genre\":\"drama\",\"shows\":[{\"title\":\"Show 2\",\"tickets_available\":100,\"price\":40.0}]}]}"
  val show2Discount = "{\"inventory\":[{\"genre\":\"drama\",\"shows\":[{\"title\":\"Show 2\",\"tickets_available\":100,\"price\":32.00}]}]}"

  val orderFixture = "{\"show\":\"Show 2\",\"performance_date\":\"2022-05-01\",\"tickets\":50}"
  val orderFixture2 = "{\"show\":\"Show 2\",\"performance_date\":\"2022-05-01\",\"tickets\":60}"
  val orderResponse1 = "{\"status\":\"success\",\"show\":\"Show 2\",\"performance_date\":\"2022-05-01\",\"tickets_bought\":50,\"tickets_available\":50}"
  val orderResponse2 = "{\"status\":\"success\",\"show\":\"Show 2\",\"performance_date\":\"2022-05-01\",\"tickets_bought\":50,\"tickets_available\":0}"
  val orderResponseFailure = "{\"status\":\"failure\",\"show\":\"Show 2\",\"performance_date\":\"2022-05-01\",\"message\":\"Not enough tickets\"}"

  val wrongShowOrder = "{\"show\":\"Show 4\",\"performance_date\":\"2022-05-01\",\"tickets\":50}"
  val wrongShowFailure = "{\"status\":\"failure\",\"show\":\"Show 4\",\"performance_date\":\"2022-05-01\",\"message\":\"Show not found\"}"

  val wrongDateOrder = "{\"show\":\"Show 2\",\"performance_date\":\"2022-12-01\",\"tickets\":50}"
  val wrongDateOrderFailure = "{\"status\":\"failure\",\"show\":\"Show 2\",\"performance_date\":\"2022-12-01\",\"message\":\"Show is not running\"}"

  override def spec = suite("Tickets App suite")(
    test("should find only Show 2") {
      for {
        res <- TicketsHttp(Request.get(URL(!! / "inventory" / "2022-05-01")))
        body <- res.body.asString
      } yield assertTrue(body == show2)
    },

    test("should find only Show 2 with discounted tickets") {
      for {
        res <- TicketsHttp(Request.get(URL(!! / "inventory" / "2022-07-20")))
        body <- res.body.asString
      } yield assertTrue(body == show2Discount)
    },

    test("should sequentially order tickets for Show 2") {
      for {
        res1 <- TicketsHttp(Request.post(Body.fromCharSequence(orderFixture), URL(!! / "inventory" / "order")))
        res2 <- TicketsHttp(Request.post(Body.fromCharSequence(orderFixture), URL(!! / "inventory" / "order")))
        body1 <- res1.body.asString
        body2 <- res2.body.asString
      } yield assertTrue(body1 == orderResponse1, body2 == orderResponse2)
    },

    test("there should not be enough tickets for Show 2") {
      for {
        res1 <- TicketsHttp(Request.post(Body.fromCharSequence(orderFixture), URL(!! / "inventory" / "order")))
        res2 <- TicketsHttp(Request.post(Body.fromCharSequence(orderFixture2), URL(!! / "inventory" / "order")))
        body1 <- res1.body.asString
        body2 <- res2.body.asString
      } yield assertTrue(body1 == orderResponse1, body2 == orderResponseFailure)
    },

    test("should not order tickets if show is missing") {
      for {
        res <- TicketsHttp(Request.post(Body.fromCharSequence(wrongShowOrder), URL(!! / "inventory" / "order")))
        body <- res.body.asString
      } yield assertTrue(body == wrongShowFailure)
    },

    test("should not order tickets if show is not running") {
      for {
        res <- TicketsHttp(Request.post(Body.fromCharSequence(wrongDateOrder), URL(!! / "inventory" / "order")))
        body <- res.body.asString
      } yield assertTrue(body == wrongDateOrderFailure)
    }

  ).provide(Inventory.test(stateFixture)) // it will provide fresh Inventory in each individual test
}
