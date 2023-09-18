package otus.diploma

import otus.diploma.db.contexts.{MainDbContext, MainDbDataSource}
import otus.diploma.db.migration.DbMigration
import otus.diploma.db.repositories.{BreedRepository, DogRepository, FosterHostRepository, VolunteerRepository}
import otus.diploma.service.DogService
import zio.ZLayer.Debug
import zio.http._
import zio.{Console, ZIO, ZIOAppDefault}

import java.sql.Date

object Main extends ZIOAppDefault {

  val app = Http.collect[Request] {
    case Method.GET -> Root / "text" => Response.text("Hello World!")
  }

  override def run = (for {
    _ <- DbMigration.migrate("db_init/main_db/changelog.xml")
    s <- ZIO.service[MainDbContext]
    q <- BreedRepository.getAll(None)
    _ <- Console.printLine(q.mkString)
    q <- BreedRepository.getAll(Some("%к%"))
    _ <- Console.printLine(q.mkString)
    q <- DogRepository.getAll(None, None)
    _ <- Console.printLine(q.mkString)
    q <- DogRepository.getAll(Some("%obik%"), Some(1))
    _ <- Console.printLine(q.mkString)
    q <- DogRepository.getAll(None, Some(1))
    _ <- Console.printLine(q.mkString)
    q <- DogRepository.getAll(Some("%sko%"), None)
    _ <- Console.printLine(q.mkString)
    d <- ZIO.some(Date.valueOf("2010-01-01"))
    q <- VolunteerRepository.getAll(None, None, d)
    _ <- Console.printLine(q.mkString)
    _ <- Server.serve(app).exit
  } yield ()).provide(
    Server.default,
    BreedRepository.live,
    DogRepository.live,
    VolunteerRepository.live,
    FosterHostRepository.live,
    DogService.live,
    MainDbDataSource.live >>> (MainDbContext.live ++ DbMigration.live),
    Debug.mermaid
  )
}
