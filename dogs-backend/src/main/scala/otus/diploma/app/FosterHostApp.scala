package otus.diploma.app

import otus.diploma.app.common.CommonApp
import otus.diploma.dto.{AddFosterHostRequest, EntityIdResponse}
import otus.diploma.service.FosterHostService
import zio._
import zio.http._
import zio.json._

object FosterHostApp extends CommonApp {
  val app = Http.collectZIO[Request] {
    case rq@Method.POST -> Root / "foster_host" / "add" => for {
      d <- rq.body.asString
      d <- resultOrFail(d.fromJson[AddFosterHostRequest])
      id <- FosterHostService.add(d)
      resp <- ZIO.attempt(EntityIdResponse(id).toJsonPretty)
    } yield Response.json(resp)

    case Method.DELETE -> Root / "foster_host" / "delete" / id => for {
      id <- ZIO.attempt(id.toLong)
      id <- FosterHostService.delete(id)
      resp <- ZIO.attempt(EntityIdResponse(id).toJsonPretty)
    } yield Response.json(resp)
  }
}
