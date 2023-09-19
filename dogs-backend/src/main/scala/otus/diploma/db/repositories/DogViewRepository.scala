package otus.diploma.db.repositories

import otus.diploma.db.contexts.MainDbContext
import otus.diploma.db.model.{Breed, Dog, DogView, FosterHost, Volunteer}
import io.getquill.Ord
import zio._

import java.sql.SQLException

case class DogViewRepository(ctx: MainDbContext) {

  import ctx._

  private val data = quote(
    query[Dog]
      .join(query[Breed]).on((d, b) => d.breedId == b.id)
      .leftJoin(query[FosterHost]).on{case ((d, _), f) => d.id == f.dogId }
      .leftJoin(query[Volunteer]).on{ case ((_, f), v) => f.exists(_.volunteerId == v.id) }
      .map { case (((d, b), _), v) =>
        DogView(d.id, d.name, d.idSerial, d.registrationDate, b.id, b.name, v.map(_.id), v.map(_.name), v.map(_.document))
      }
  )

  final val entityName = "Информация о собаках"

  def getAll(name: Option[String], breedName: Option[String], volunteerName: Option[String],
             volunteerDocument: Option[String]): ZIO[Any, SQLException, List[DogView]] = run {
    data
      .filter { case (d) =>
        lift(name.map(_.toLowerCase)).forall(n => d.name.toLowerCase like n) &&
          lift(breedName.map(_.toLowerCase)).forall(n => d.breedName.toLowerCase like n) &&
          lift(volunteerName.map(_.toLowerCase)).forall(n => d.volunteerName.exists(_.toLowerCase like n)) &&
          lift(volunteerDocument.map(_.toLowerCase)).forall(n => d.volunteerDocument.exists(_.toLowerCase like n))
      }
      .sortBy{ case (d) => (d.name, d.registrationDate)}(Ord.asc)
  }
}

object DogViewRepository{
  def getAll(name: Option[String], breedName: Option[String], volunteerName: Option[String],
             volunteerDocument: Option[String]): ZIO[DogViewRepository, SQLException, List[DogView]] =
    ZIO.serviceWithZIO[DogViewRepository](_.getAll(name, breedName, volunteerName, volunteerDocument))

  val live = ZLayer.fromFunction(DogViewRepository(_))
}
