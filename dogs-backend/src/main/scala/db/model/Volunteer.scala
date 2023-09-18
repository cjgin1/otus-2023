package db.model

import db.model.common.CommonModel

import java.sql.{Date, Timestamp}

case class Volunteer(override val id: Long = 0,
                     name: String,
                     birthdate: Date,
                     document: String,
                     registrationDate: Timestamp) extends CommonModel
