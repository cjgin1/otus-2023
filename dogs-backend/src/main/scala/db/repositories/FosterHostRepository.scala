package db.repositories

import db.contexts.MainDbContext
import db.model.FosterHost
import db.repositories.common.CommonRepository
import zio._

import java.sql.SQLException

case class FosterHostRepository(ctx: MainDbContext) extends CommonRepository[FosterHost]{
  import ctx._

  private val data = quote(query[FosterHost])

  final val entityName = "Собаки у приемных хозяев"

  def getAll: ZIO[Any, SQLException, List[FosterHost]] = run {
    data
  }

  def getById(id: Long): ZIO[Any, SQLException, List[FosterHost]] = run {
    data.filter(_.id == lift(id)).take(1)
  }

  def getByDogId(id: Long): ZIO[Any, SQLException, List[FosterHost]] = run {
    data.filter(_.dogId == lift(id)).take(1)
  }

  def add(entity: FosterHost): ZIO[Any, SQLException, Long] = run {
    data.insertValue(lift(entity)).returningGenerated(_.id)
  }

  def delete(id: Long): ZIO[Any, SQLException, Long] = run {
    data.filter(_.id == lift(id)).delete.returning(_.id)
  }
}

object FosterHostRepository{
  private val service = ZIO.serviceWithZIO[FosterHostRepository]

  def getAll: ZIO[FosterHostRepository, SQLException, List[FosterHost]] =
    service(_.getAll)

  def add(entity: FosterHost): ZIO[FosterHostRepository, SQLException, Long] =
    service(_.add(entity))

  def delete(id:Long): ZIO[FosterHostRepository, SQLException, Long] =
    service(_.delete(id))

  val live = ZLayer.fromFunction(FosterHostRepository(_))
}