package mappers.common

private[mappers] trait CommonMapper[M, D] {
  def mapModel(model: M): D
}
