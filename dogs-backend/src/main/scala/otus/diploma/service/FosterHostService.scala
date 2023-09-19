package otus.diploma.service

import otus.diploma.db.contexts.MainDbContext
import otus.diploma.db.model.FosterHost
import otus.diploma.db.repositories.{DogRepository, FosterHostRepository, VolunteerRepository}
import otus.diploma.dto.AddFosterHostRequest
import otus.diploma.service.common.CommonService
import zio.{ZIO, ZLayer}

case class FosterHostService(ctx: MainDbContext, fosterHostRepository: FosterHostRepository, dogRepository: DogRepository,
                             volunteerRepository: VolunteerRepository) extends CommonService {
  def add(rq: AddFosterHostRequest): ZIO[Any, Throwable, Long] = transactionZIO {
    for {
      _ <- dogRepository.forUpdate(rq.dogId)
      dogs <- dogRepository.getById(rq.dogId)
      dog <- singleEntity(dogs, dogRepository.entityName)
      fhs <- fosterHostRepository.getByDogId(dog.id)
      fh <- singleEntityOrNone(fhs, fosterHostRepository.entityName)
      _ <- ZIO.foreach(fh)(_ => ZIO.fail(new Exception("Выбранная собака уже отдана новым хозяевам.")))
      vols <- volunteerRepository.getById(rq.fosterHostId)
      vol <- singleEntity(vols, volunteerRepository.entityName)
      ts <- currentTimestamp()
      id <- fosterHostRepository.add(FosterHost(volunteerId = vol.id, dogId = dog.id, registrationDate = ts))
    } yield id
  }


}

object FosterHostService{
  private val service = ZIO.serviceWithZIO[FosterHostService]

  def add(rq: AddFosterHostRequest): ZIO[FosterHostService, Throwable, Long] =
    service(_.add(rq))

  val live = ZLayer.fromFunction(FosterHostService(_, _, _, _))
}