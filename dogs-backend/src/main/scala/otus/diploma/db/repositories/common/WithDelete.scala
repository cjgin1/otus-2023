package otus.diploma.db.repositories.common

import zio._

import java.sql.SQLException

trait WithDelete[T] {
  def delete(id: Long): ZIO[Any, SQLException, Long]
}
