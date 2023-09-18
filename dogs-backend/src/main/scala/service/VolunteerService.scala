package service

import db.contexts.MainDbContext
import db.repositories.VolunteerRepository
import dto.{AddVolunteerRequest, VolunteerResponse}
import db.model.Volunteer
import mappers.All._
import service.common.CommonService
import zio._

import java.sql.Date

case class VolunteerService(ctx: MainDbContext, volunteerRepository: VolunteerRepository) extends CommonService {
  def getAll(name: Option[String], document: Option[String],
             birthDate: Option[Date]): ZIO[Any, Throwable, List[VolunteerResponse]] = transactionZIO {
    volunteerRepository.getAll(name.map(likePattern),document.map(likePattern), birthDate)
  }

  def add(rq: AddVolunteerRequest): ZIO[Any, Throwable, Long] = transactionZIO{
    for{
      ts <- currentTimestamp
      id <- volunteerRepository.add(
        Volunteer(
          name = rq.name,
          document = rq.document,
          birthdate = rq.birthDate,
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
