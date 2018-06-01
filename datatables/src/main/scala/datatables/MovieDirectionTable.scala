package datatables
import slick.jdbc.PostgresProfile.api._

class MovieDirectionTable(tag: Tag) extends Table[model.MovieDirection](tag,"movie_direction"){
  def directorId = column[Int]("dir_id")
  def movieId = column[Int]("mov_id")
  def * =
    (directorId, movieId) <> ((model.MovieDirection.apply _).tupled, model.MovieDirection.unapply)

  val directorForeignKey =
    foreignKey("fk_director_movieDirection", directorId, DirectorTable.table) (
      _.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade
    )

  val movieForeignKey =
    foreignKey("fk_movie_movieDirection", movieId, MovieTable.table) (
      _.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade
    )
}

object MovieDirectionTable{
  val table = TableQuery[MovieDirectionTable]
}
