package otus.diploma.db.repositories.common

import zio._

import java.sql.SQLException

trait WithAdd[T] {
  def add(entity: T):  ZIO[Any, SQLException, Long]
}
