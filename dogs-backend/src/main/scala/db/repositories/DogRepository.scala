package db.repositories

import db.contexts.MainDbContext
import db.model.Dog
import db.repositories.common.{CommonRepository, WithForUpdate}
import io.getquill.Ord
import zio._

import java.sql.SQLException

case class DogRepository(ctx: MainDbContext) extends CommonRepository[Dog] with WithForUpdate[Dog]{
  import ctx._

  private val data = quote(query[Dog])

  final val entityName = "Собаки"

  def getAll(name: Option[String], breedId: Option[Long]) : ZIO[Any, SQLException, List[Dog]] = run {
    data
      .filter(d =>
        lift(name.map(_.toLowerCase)).forall(n => d.name.toLowerCase like n) &&
          lift(breedId).forall(n => d.breedId == n)
      )
      .sortBy(x => (x.name, x.registrationDate))(Ord.asc)

  }

  def getById(id: Long): ZIO[Any, SQLException, List[Dog]] = run {
    data.filter(_.id == lift(id)).take(1)
  }

  def forUpdate(id: Long): ZIO[Any, SQLException, List[Dog]] = run{
    data.filter(_.id == lift(id)).take(1).forUpdate()
  }

  def add(entity: Dog): ZIO[Any, SQLException, Long] = run {
    data.insertValue(lift(entity)).returningGenerated(_.id)
  }

  def delete(id: Long): ZIO[Any, SQLException, Long] = run {
    data.filter(_.id == lift(id)).delete.returning(_.id)
  }
}

object DogRepository {
  private val service = ZIO.serviceWithZIO[DogRepository]

  def getAll(name: Option[String], breedId: Option[Long]): ZIO[DogRepository, Exception, List[Dog]] =
    service(_.getAll(name, breedId))

  def add(entity: Dog): ZIO[DogRepository, SQLException, Long] =
    service(_.add(entity))

  def delete(id: Long): ZIO[DogRepository, SQLException, Long] =
    service(_.delete(id))

  val live = ZLayer.fromFunction(DogRepository(_))
}