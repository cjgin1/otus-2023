package db.repositories.common

import zio._

import java.sql.SQLException

trait WithGetById[T] {
  def getById(id: Long): ZIO[Any, SQLException, List[T]]
}
