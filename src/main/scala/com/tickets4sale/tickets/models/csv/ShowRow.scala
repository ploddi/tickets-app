package com.tickets4sale.tickets.models.csv

import com.tickets4sale.tickets.models.Genre
import kantan.csv._

import java.text.SimpleDateFormat
import java.time.{LocalDate, ZoneId}

case class ShowRow(title: String, openingDate: LocalDate, genre: Genre)

object ShowRow {

  implicit val dateDecode: CellDecoder[LocalDate] =
    CellDecoder
      .dateDecoder(new SimpleDateFormat("yyyy-MM-ddg"))
      .map(d => LocalDate.ofInstant(d.toInstant, ZoneId.systemDefault()))

  implicit val rowDecoder: RowDecoder[ShowRow] = RowDecoder.ordered(ShowRow.apply _)

}
