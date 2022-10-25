package com.tickets4sale.tickets.models

import kantan.csv.{CellDecoder, DecodeError}

sealed abstract class Genre(val value: String)

object Genre {

  final case object Musicals extends Genre("musicals")

  final case object Comedy extends Genre("comedy")

  final case object Drama extends Genre("drama")

  implicit val cellDecoder: CellDecoder[Genre] = CellDecoder.from {
    case "MUSICAL" => Right(Musicals)
    case "COMEDY" => Right(Comedy)
    case "DRAMA" => Right(Drama)
    case other => Left(DecodeError.TypeError(s"Invalid genre value: $other"))
  }

}
