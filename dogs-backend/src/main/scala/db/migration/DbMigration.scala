package db.migration

import liquibase._
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import zio._

import javax.sql.DataSource

case class DbMigration(dataSource: DataSource) {
  def migrate(changelog: String): ZIO[Any, Throwable, Unit] = ZIO.logSpan("Migrating database") {
    ZIO.scoped {
      for {
        _ <- ZIO.logInfo("Getting connection...")
        conn <- ZIO.acquireRelease(ZIO.attempt(dataSource.getConnection))(x => ZIO.attempt(x.close()).orDie)
        auto <- ZIO.attempt(conn.getAutoCommit)
        _ <- ZIO.logInfo("Configuring liquibase...")
        liquibase <- ZIO.attempt(new Liquibase(changelog, new ClassLoaderResourceAccessor(), new JdbcConnection(conn)))
        _ <- ZIO.logInfo("Updating database...")
        _ <- ZIO.attempt(conn.setAutoCommit(false))
        _ <- ZIO.attempt(liquibase.update()).catchAll(ex =>
          ZIO.logInfo("Error when migrating DB - rolling back...") *> ZIO.attempt(conn.rollback()) *>
            ZIO.fail(ex)
        )
        _ <- ZIO.logInfo("Executing commit...")
        _ <- ZIO.attempt(conn.commit())
        _ <- ZIO.attempt(conn.setAutoCommit(auto))
        _ <- ZIO.logInfo("Migration is complete.")
      } yield ()
    }
  }
}

object DbMigration{
  def migrate(changelog: String) = ZIO.serviceWithZIO[DbMigration](_.migrate(changelog))

 val live = ZLayer.fromFunction(DbMigration(_))
}