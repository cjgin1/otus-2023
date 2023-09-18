package otus.diploma.db.model

import otus.diploma.db.model.common.CommonModel

import java.sql.Timestamp

case class FosterHost(override val id: Long = 0,
                      volunteerId: Long,
                      dogId: Long,
                      registrationDate: Timestamp) extends CommonModel
