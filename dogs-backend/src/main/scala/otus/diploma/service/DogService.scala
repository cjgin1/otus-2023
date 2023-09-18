package otus.diploma.service

import otus.diploma.db.contexts.MainDbContext
import otus.diploma.db.model.{Dog, FosterHost}
import otus.diploma.db.repositories._
import otus.diploma.dto.{AddDogRequest, AddFosterHostRequest, DogResponse}
import otus.diploma.mappers.All._
import otus.diploma.service.common.CommonService
import zio.{ZIO, ZLayer}

case class DogService(ctx: MainDbContext, dogRepository: DogRepository, dogViewRepository: DogViewRepository,
                      breedRepository: BreedRepository) extends CommonService{
  def getAll(name: Option[String], breedName: Option[String]): ZIO[Any, Throwable, List[DogResponse]] = transactionZIO {
    dogViewRepository.getAll(name.map(likePattern), breedName.map(likePattern))
  }

  def add(rq: AddDogRequest): ZIO[BreedRepository, Throwable, Unit] = transactionZIO {
    for {
      bName <- ZIO.some(likePattern(rq.breedName))
      breeds <- BreedRepository.getAll(bName)
      b <- singleEntity(breeds, breedRepository.entityName)
      uuid <- genUUID
      ts <- currentTimestamp
      _ <- dogRepository.add(
        Dog(
          name = rq.name,
          idSerial = uuid,
          registrationDate = ts,
          breedId = b.id)
      )
    } yield()
  }

  def delete(id: Long): ZIO[Any, Throwable, Long] = transactionZIO {
    for{
      _ <- dogRepository.forUpdate(id)
      dogs <- dogRepository.getById(id)
      _ <- singleEntity(dogs, dogRepository.entityName)
      id <- dogRepository.delete(id)
    } yield id
  }
}

object DogService{
  def getAll(name: Option[String], breedName: Option[String]): ZIO[DogService, Throwable, List[DogResponse]] =
    ZIO.serviceWithZIO[DogService](_.getAll(name, breedName))

  def add(rq: AddDogRequest): ZIO[BreedRepository with DogService, Throwable, Unit] =
    ZIO.serviceWithZIO[DogService](_.add(rq))

  val live = ZLayer.fromFunction(DogService(_, _, _, _))
}