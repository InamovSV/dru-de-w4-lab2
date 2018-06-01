package datatables
import slick.jdbc.PostgresProfile.api._

class MovieCastTable(tag: Tag) extends Table[model.MovieCast](tag,"movie_cast"){
  def actorId = column[Int]("act_id")
  def movieId = column[Int]("mov_id")
  def role = column[String]("role", O.SqlType("varchar(30)"))
  def * =
    (actorId, movieId, role) <> ((model.MovieCast.apply _).tupled, model.MovieCast.unapply)

  val movieForeignKey =
    foreignKey("fk_movie_cast", movieId, MovieTable.table) (
      _.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade
    )

  val actorForeignKey =
    foreignKey("fk_actor_cast", actorId, ActorTable.table) (
      _.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade
    )
}

object MovieCastTable{
  val table = TableQuery[MovieCastTable]
}
