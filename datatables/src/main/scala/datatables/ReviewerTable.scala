package datatables
import slick.jdbc.PostgresProfile.api._

class ReviewerTable(tag: Tag) extends Table[model.Reviewer](tag, "reviewer"){
  def id = column[Int]("rev_id", O.PrimaryKey)
  def name = column[String]("rev_name", O.SqlType("varchar(30)"))
  def * =
    (id, name) <> ((model.Reviewer.apply _).tupled, model.Reviewer.unapply)
}

object ReviewerTable{
  val table = TableQuery[ReviewerTable]
}
