package otus.diploma.db.repositories

import otus.diploma.db.contexts.MainDbContext
import otus.diploma.db.model.Volunteer
import otus.diploma.db.repositories.common.{CommonRepository, WithForUpdate}
import otus.diploma.db.repositories.functions.StdFunctions.dateFuncS
import zio._

import java.sql.{Date, SQLException}

case class VolunteerRepository(ctx: MainDbContext) extends CommonRepository[Volunteer] with WithForUpdate[Volunteer] {
  import ctx._

  private val data = quote(query[Volunteer])


  final val entityName = "Волонтеры"

  def getAll(name: Option[String], document: Option[String], birthDate: Option[Date]): ZIO[Any, SQLException, List[Volunteer]] = {
    val bt = birthDate.map(_.toString)
    run {
      data.filter(d =>
        lift(name.map(_.toLowerCase)).forall(v => d.name.toLowerCase like v) &&
          lift(document.map(_.toLowerCase)).forall(v => d.document like v) &&
          lift(bt).forall(v => d.birthdate == dateFuncS(v))
      )
    }
  }

  def getById(id: Long): ZIO[Any, SQLException, List[Volunteer]] = run {
    data.filter(_.id == lift(id)).take(1)
  }

  def forUpdate(id: Long): ZIO[Any, SQLException, List[Volunteer]] = run {
    data.filter(_.id == lift(id)).take(1).forUpdate()
  }

  def add(entity: Volunteer): ZIO[Any, SQLException, Long] = run {
    data.insertValue(lift(entity)).returningGenerated(_.id)
  }

  def delete(id: Long): ZIO[Any, SQLException, Long] = run {
    data.filter(_.id == lift(id)).delete.returning(_.id)
  }
}

object VolunteerRepository {
  def getAll(name: Option[String], document: Option[String],
             birthDate: Option[Date]): ZIO[VolunteerRepository, Exception, List[Volunteer]] =
    ZIO.serviceWithZIO[VolunteerRepository](_.getAll(name, document, birthDate))

  def add(entity: Volunteer): ZIO[VolunteerRepository, SQLException, Long] = ZIO.serviceWithZIO[VolunteerRepository](_.add(entity))

  def delete(id: Long): ZIO[VolunteerRepository, SQLException, Long] = ZIO.serviceWithZIO[VolunteerRepository](_.delete(id))

  val live = ZLayer.fromFunction(VolunteerRepository(_))
}
