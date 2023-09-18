package db.contexts

import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill.Postgres
import zio._
import javax.sql.DataSource

case class MainDbContext(dataSource: DataSource) extends Postgres(SnakeCase, dataSource){

}

object MainDbContext{
  val live = ZLayer.fromFunction(MainDbContext(_))
}
