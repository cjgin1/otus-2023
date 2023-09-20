package otus.diploma.db.contexts

import io.getquill.jdbczio.Quill.DataSource

object MainDbDataSource {
  val live = DataSource.fromPrefix("postgres")
}
