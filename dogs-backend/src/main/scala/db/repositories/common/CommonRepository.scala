package db.repositories.common

trait CommonRepository[T] extends WithGetById[T] with WithAdd[T] with WithDelete[T]
