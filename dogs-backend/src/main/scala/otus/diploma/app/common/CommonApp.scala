package otus.diploma.app.common

import zio._
import zio.http.{Form, Request}

trait CommonApp {
  def paramsToForm(rq: Request): Task[Form] = ZIO.attempt(rq.url.queryParams.toForm)

  def formParam(name: String, form: Form): Task[Option[String]] = ZIO.attempt(form.get(name).flatMap(_.stringValue))

  def resultOrFail[R](either: Either[String, R]): ZIO[Any, Exception, R] = either.fold(
    e => ZIO.fail(new Exception(e)),
    r => ZIO.succeed(r)
  )

}
