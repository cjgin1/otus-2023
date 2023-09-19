package otus.diploma.app

import otus.diploma.app.common.CommonApp
import otus.diploma.dto.{AddDogRequest, DogResponseWrapper, EntityIdResponse}
import otus.diploma.service.DogService
import zio._
import zio.http._
import zio.json._

object DogApp extends CommonApp {
  val app = Http.collectZIO[Request] {
    case rq@Method.GET -> Root / "dog" / "all" => for {
      params <- paramsToForm(rq)
      name <- formParam("name", params)
      breedName <- formParam("breed_name", params)
      volunteerName <- formParam("volunteer_name", params)
      volunteerDocument <- formParam("volunteer_document", params)
      resp <- DogService.getAll(name, breedName, volunteerName, volunteerDocument)
      resp <- ZIO.attempt(DogResponseWrapper(resp).toJsonPretty)
    } yield Response.json(resp)


    case Method.DELETE -> Root / "dog" / "delete" / id => for {
      id <- ZIO.attempt(id.toLong)
      id <- DogService.delete(id)
      resp <- ZIO.attempt(EntityIdResponse(id).toJsonPretty)
    } yield Response.json(resp)


    case rq@Method.POST -> Root / "dog" / "add" => for {
      d <- rq.body.asString
      d <- resultOrFail(d.fromJson[AddDogRequest])
      id <- DogService.add(d)
      resp <- ZIO.attempt(EntityIdResponse(id).toJsonPretty)
    } yield Response.json(resp)
  }
}
