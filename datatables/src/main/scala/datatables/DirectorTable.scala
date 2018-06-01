package datatables
import slick.jdbc.PostgresProfile.api._

class DirectorTable(tag: Tag) extends Table[model.Director](tag,"director"){
  def id = column[Int]("dir_id", O.PrimaryKey)
  def firstName = column[String]("dir_fname", O.SqlType("varchar(20)"))
  def lastName = column[String]("dir_lname", O.SqlType("varchar(20)"))
  def * =
    (id, firstName, lastName) <> ((model.Director.apply _).tupled, model.Director.unapply)
}

object DirectorTable{
  val table = TableQuery[DirectorTable]
}
