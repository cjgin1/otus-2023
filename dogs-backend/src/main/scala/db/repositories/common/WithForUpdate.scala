package db.repositories.common

import zio._

import java.sql.SQLException

trait WithForUpdate[T] {
  def forUpdate(id: Long): ZIO[Any, SQLException, List[T]]
}
