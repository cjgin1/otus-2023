package db.repositories

import db.contexts.MainDbContext
import db.model.Breed
import db.repositories.common.CommonRepository
import io.getquill.Ord
import zio._

import java.sql.SQLException

// Код в репозиториях будет однообразным, т.к. не получается сделать CommonRepository со всеми стандартными методами
// trait CommonRepository[TEntity <: CommonModel: Tag : SchemaMeta] или как-то еще добавить простым способом
// implicit SchemaMeta для любого типа. Это связано с кодогенерацией quill и его поддержкой версий Скалы.
// https://stackoverflow.com/questions/40563641/cant-find-an-implicit-schemameta-for-type
// Error:(14, 12) Can't find an implicit `SchemaMeta` for type `com.abhi.Movies`
//      query[Movies]
// Если бы не эта проблема, все простые репозитории включали бы в себя только наследование от базового и функцию для реализации
// live в объекте-компаньоне. Также могли бы быть дополнены специфичными для них функциями.

case class BreedRepository(ctx: MainDbContext) extends CommonRepository[Breed]{

  import ctx._

  private val data = quote(query[Breed])

  final val entityName = "Породы"

  def getAll(name: Option[String]): ZIO[Any, SQLException, List[Breed]] = {
    run {
      data.filter(x => lift(name.map(_.toLowerCase)).forall(n => x.name.toLowerCase like n)).sortBy(_.name)(Ord.asc)
    }
  }

  def getById(id: Long): ZIO[Any, SQLException, List[Breed]] = run {
    data.filter(_.id == lift(id)).take(1)
  }

  def add(entity: Breed): ZIO[Any, SQLException, Long] = run {
    data.insertValue(lift(entity)).returningGenerated(_.id)
  }

  def delete(id: Long): ZIO[Any, SQLException, Long] = run {
    data.filter(_.id == lift(id)).delete.returning(_.id)
  }
}

object BreedRepository {
  private val service = ZIO.serviceWithZIO[BreedRepository]

  def getAll(name: Option[String]): ZIO[BreedRepository, SQLException, List[Breed]] = service(_.getAll(name))

  def add(entity: Breed): ZIO[BreedRepository, SQLException, Long] = service(_.add(entity))

  def delete(id: Long): ZIO[BreedRepository, SQLException, Long] = service(_.delete(id))

  val live = ZLayer.fromFunction(BreedRepository(_))
}
