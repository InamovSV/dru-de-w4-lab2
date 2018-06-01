package datatables
import slick.jdbc.PostgresProfile.api._

class MovieGenreTable(tag: Tag) extends Table[model.MovieGenre](tag, "movie_genres") {
  def movieId = column[Int]("mov_id")
  def genreId =  column[Int]("gen_id")
  def * =
    (movieId, genreId) <> ((model.MovieGenre.apply _).tupled, model.MovieGenre.unapply)

  val genreForeignKey =
    foreignKey("fk_director_movieDirection", genreId, GenresTable.table) (
      _.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade
    )

  val movieForeignKey =
    foreignKey("fk_movie_movieDirection", movieId, MovieTable.table) (
      _.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade
    )
}

object MovieGenreTable{
  val table = TableQuery[MovieGenreTable]
}
