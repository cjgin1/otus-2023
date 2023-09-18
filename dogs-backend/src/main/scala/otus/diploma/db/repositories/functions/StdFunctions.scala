package otus.diploma.db.repositories.functions

import io.getquill.mirrorContextWithQueryProbing.{SqlInfixInterpolator, quote}

import java.sql.Date

object StdFunctions {
  // fix quill date/timestamp bug
  // org.postgresql.util.PSQLException: ERROR: could not determine data type of parameter $7
  // quill is not mapping parameter types in the right way with optional filters
  // https://github.com/zio/zio-quill/issues/121
  // Static filters do not need such a conversion
  val dateFuncS = quote( (d: String) => sql"date($d)".as[Date])

  // fix quill date/timestamp bug
  // org.postgresql.util.PSQLException: ERROR: could not determine data type of parameter $7
  // quill is not mapping parameter types in the right way with optional filters
  // https://github.com/zio/zio-quill/issues/121
  // Static filters do not need such a conversion
  val timestampS = quote((d: String) => sql"$d::timestamp".as[Date])
}
