package otus.diploma.service

import otus.diploma.db.contexts.MainDbContext
import otus.diploma.db.repositories.VolunteerRepository
import otus.diploma.dto.{AddVolunteerRequest, VolunteerResponse}
import otus.diploma.db.model.Volunteer
import otus.diploma.mappers.All._
import otus.diploma.service.common.CommonService
import zio._

import java.sql.Date

case class VolunteerService(ctx: MainDbContext, volunteerRepository: VolunteerRepository) extends CommonService {
  def getAll(name: Option[String], document: Option[String],
             birthDate: Option[Date]): ZIO[Any, Throwable, List[VolunteerResponse]] = transactionZIO {
    volunteerRepository.getAll(name.map(likePattern),document.map(likePattern), birthDate)
  }

  def add(rq: AddVolunteerRequest): ZIO[Any, Throwable, Long] = transactionZIO{
    for{
      ts <- currentTimestamp()
      birthdate <- ZIO.attempt(Date.valueOf(rq.birthdate))
      id <- volunteerRepository.add(
        Volunteer(
          name = rq.name,
          document = rq.document,
          birthdate = birthdate,
          registrationDate = ts
        )
      )
    } yield id
  }

  def delete(id: Long): ZIO[Any, Throwable, Long] = transactionZIO {
    for {
      _ <- volunteerRepository.forUpdate(id)
      dogs <- volunteerRepository.getById(id)
      _ <- singleEntity(dogs, volunteerRepository.entityName)
      id <- volunteerRepository.delete(id)
    } yield id
  }
}

object VolunteerService{
  private val service = ZIO.serviceWithZIO[VolunteerService]
  def getAll(name: Option[String], document: Option[String],
             birthDate: Option[Date]): ZIO[VolunteerService, Throwable, List[VolunteerResponse]] =
    service(_.getAll(name, document, birthDate))

  def add(rq: AddVolunteerRequest): ZIO[VolunteerService, Throwable, Long] =
    service(_.add(rq))

  def delete(id: Long): ZIO[VolunteerService, Throwable, Long] =
    service(_.delete(id))

  val live = ZLayer.fromFunction(VolunteerService(_, _))
}
