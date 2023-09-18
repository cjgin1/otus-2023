package otus.diploma.db.repositories

import otus.diploma.db.contexts.MainDbContext
import otus.diploma.db.model.{Breed, Dog, DogView}
import io.getquill.Ord
import zio._

import java.sql.SQLException

case class DogViewRepository(ctx: MainDbContext) {

  import ctx._

  private val data = quote(query[Dog].join(query[Breed]).on((d, b) => d.breedId == b.id))

  final val entityName = "Информация о собаках"

  def getAll(name: Option[String], breedName: Option[String]): ZIO[Any, SQLException, List[DogView]] = run {
    data
      .filter { case (d, b) =>
        lift(name.map(_.toLowerCase)).forall(n => d.name.toLowerCase like n) &&
          lift(breedName).forall(n => b.name == n)
      }
      .sortBy{ case (d, b) => (d.name, d.registrationDate)}(Ord.asc).map{ case (d, b) =>
        DogView(d.id, d.name, d.idSerial, d.registrationDate, b.id, b.name)
      }

  }
}

object DogViewRepository{
  def getAll(name: Option[String], breedName: Option[String]): ZIO[DogViewRepository, SQLException, List[DogView]] =
    ZIO.serviceWithZIO[DogViewRepository](_.getAll(name, breedName))

  val live = ZLayer.fromFunction(DogViewRepository(_))
}
