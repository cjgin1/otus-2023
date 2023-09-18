package db.model

import db.model.common.CommonModel

case class Breed(override val id: Long = 0, name: String) extends CommonModel

