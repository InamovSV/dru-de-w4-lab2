package datatables
import slick.jdbc.PostgresProfile.api._

class ActorTable(tag: Tag) extends Table[model.Actor](tag, "actor"){
  def id = column[Int]("act_id", O.PrimaryKey)
  def firstName = column[String]("act_fname", O.SqlType("varchar(20)"))
  def lastName = column[String]("act_lname", O.SqlType("varchar(20)"))
  def gender = column[String]("act_gender", O.SqlType("varchar(1)"))
  def * =
    (id, firstName, lastName, gender) <> ((model.Actor.apply _).tupled, model.Actor.unapply)
}

object ActorTable{
  val table = TableQuery[ActorTable]
}


