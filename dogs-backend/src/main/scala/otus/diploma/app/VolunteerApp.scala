package otus.diploma.app

import otus.diploma.app.common.CommonApp
import otus.diploma.dto.{AddVolunteerRequest, EntityIdResponse, VolunteerResponseWrapper}
import otus.diploma.service.VolunteerService
import zio.http._
import zio._
import zio.json._

import java.sql.Date

object VolunteerApp extends CommonApp {
  val app = Http.collectZIO[Request] {
    case rq@Method.GET -> Root / "volunteer" / "all" => for {
      params <- paramsToForm(rq)
      name <- formParam("name", params)
      document <- formParam("document", params)
      birthdate <- formParam("birthdate", params)
      birthdate <- ZIO.attempt(birthdate.map(Date.valueOf))
      resp <- VolunteerService.getAll(name, document, birthdate)
      resp <- ZIO.attempt(VolunteerResponseWrapper(resp).toJsonPretty)
    } yield Response.json(resp)


    case Method.DELETE -> Root / "volunteer" / "delete" / id => for {
      id <- ZIO.attempt(id.toLong)
      id <- VolunteerService.delete(id)
      resp <- ZIO.attempt(EntityIdResponse(id).toJsonPretty)
    } yield Response.json(resp)


    case rq@Method.POST -> Root / "volunteer" / "add" => for {
      d <- rq.body.asString
      d <- resultOrFail(d.fromJson[AddVolunteerRequest])
      id <- VolunteerService.add(d)
      resp <- ZIO.attempt(EntityIdResponse(id).toJsonPretty)
    } yield Response.json(resp)
  }
}
