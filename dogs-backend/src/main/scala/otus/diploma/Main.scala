package otus.diploma

import otus.diploma.app.{DogApp, FosterHostApp, VolunteerApp}
import otus.diploma.db.contexts.{MainDbContext, MainDbDataSource}
import otus.diploma.db.migration.DbMigration
import otus.diploma.db.repositories.{BreedRepository, DogRepository, DogViewRepository, FosterHostRepository, VolunteerRepository}
import otus.diploma.service.{DogService, FosterHostService, VolunteerService}
import zio.ZLayer.Debug
import zio.http._
import zio._

object Main extends ZIOAppDefault {

  private val app = (DogApp.app ++ VolunteerApp.app ++ FosterHostApp.app) @@
    HttpAppMiddleware.addHeader("http-app", "dogs-backend") @@ HttpAppMiddleware.debug

  private val program = DbMigration.migrate("db_init/main_db/changelog.xml") *> Server.serve(app.withDefaultErrorResponse)

  override def run: ZIO[Any, Throwable, Unit] = for {
    host <- ZIO.config(Config.string("host"))
    port <- ZIO.config(Config.int("port"))
    _ <- program.provide(
      // server
      Server.defaultWith(_.binding(host, port)),
      //repos
      BreedRepository.live,
      DogRepository.live,
      DogViewRepository.live,
      VolunteerRepository.live,
      FosterHostRepository.live,
      // services
      DogService.live,
      VolunteerService.live,
      FosterHostService.live,
      // db
      MainDbDataSource.live >>> (MainDbContext.live ++ DbMigration.live),
      // debug deps
      Debug.mermaid
    )
  } yield ()
}
