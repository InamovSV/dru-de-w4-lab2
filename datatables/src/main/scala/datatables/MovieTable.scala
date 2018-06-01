package datatables
import java.time.LocalDate

import slick.jdbc.PostgresProfile.api._

class MovieTable(tag: Tag) extends Table[model.Movie](tag, "movie") {
  def id = column[Int]("mov_id", O.PrimaryKey)
  def title = column[String]("mov_title", O.SqlType("varchar(50)"))
  def year = column[Int]("mov_year")
  def time = column[Int]("mov_time")
  def language = column[String]("mov_lang", O.SqlType("varchar(50)"))
  def releaseDate = column[Option[LocalDate]]("mov_dt_rel")
  def releaseCountry = column[String]("mov_rel_country", O.SqlType("varchar(5)"))
  def * =
    (id, title, year, time, language, releaseDate, releaseCountry) <>
      ((model.Movie.apply _).tupled, model.Movie.unapply)
}

object MovieTable{
  val table = TableQuery[MovieTable]
}
