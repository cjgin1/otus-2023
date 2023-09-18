package otus.diploma.service.common

import otus.diploma.db.contexts.MainDbContext
import zio._

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

trait CommonService {
  val ctx: MainDbContext

  def transactionZIO[R, A](action: ZIO[R, Throwable, A]): ZIO[R, Throwable, A]  =
    ZIO.blocking(ctx.transaction(action))

  def likePattern(value: String): String = {
    s"%$value%"
  }

  def singleEntity[T](list: List[T], entityName: String): ZIO[Any, Throwable, T] = for {
    res <- list match{
      case list if list.length == 1 =>
        ZIO.succeed(list.head)
      case list if list.isEmpty =>
        ZIO.fail(new Exception(s"Не найдены элементы '$entityName'."))
      case _ =>
        ZIO.fail(new Exception(s"Найден более чем один элемент '$entityName'."))
    }
  } yield res

  def singleEntityOrNone[T](list: List[T], entityName: String): ZIO[Any, Throwable, Option[T]] = for {
    res <- list match {
      case list if list.length == 1 =>
        ZIO.some(list.head)
      case list if list.isEmpty =>
        ZIO.none
      case _ =>
        ZIO.fail(new Exception(s"Найден более чем один элемент '$entityName'."))
    }
  } yield res

  def genUUID: Task[UUID] = ZIO.attempt(UUID.randomUUID())

  def currentTimestamp: Task[Timestamp] = ZIO.attempt(Timestamp.valueOf(LocalDateTime.now()))
}
