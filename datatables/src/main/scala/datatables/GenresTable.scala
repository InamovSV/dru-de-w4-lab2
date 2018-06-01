package datatables
import slick.jdbc.PostgresProfile.api._

class GenresTable(tag: Tag) extends Table[model.Genre](tag, "genres"){
  def id = column[Int]("gen_id", O.PrimaryKey)
  def title = column[String]("gen_title", O.SqlType("varchar(20)"))
  def * =
    (id, title) <> ((model.Genre.apply _).tupled, model.Genre.unapply)
}

object GenresTable{
  val table = TableQuery[GenresTable]
}
