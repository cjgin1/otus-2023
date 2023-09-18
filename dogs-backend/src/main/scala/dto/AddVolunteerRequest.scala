package dto

import java.sql.{Date, Timestamp}

case class AddVolunteerRequest(name: String, document: String, birthDate: Date)
