package mappers.common

import zio.ZIO

private[mappers] trait Common {
  implicit def modelList2DTOList[M, D](that: List[M])(implicit mapper: CommonMapper[M, D]): List[D] = {
    that.map(mapper.mapModel)
  }

  implicit def modelList2DTOListZIO[M, D](that: ZIO[Any, Throwable, List[M]])(implicit commonMapper: CommonMapper[M, D]): ZIO[Any, Throwable, List[D]] = {
    that.flatMap(x => ZIO.attempt(x))
  }
}
