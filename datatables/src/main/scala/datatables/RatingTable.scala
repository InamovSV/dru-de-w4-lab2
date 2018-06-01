package datatables
import slick.jdbc.PostgresProfile.api._

class RatingTable(tag: Tag) extends Table[model.Rating](tag,"rating"){
  def movieId = column[Int]("mov_id")
  def reviewerId = column[Int]("rev_id")
  def reviewStars = column[Option[Double]]("rev_stars")
  def numberOfRatings = column[Option[Int]]("num_o_ratings")
  def * =
    (movieId, reviewerId, reviewStars, numberOfRatings) <>
      ((model.Rating.apply _).tupled, model.Rating.unapply)

  val reviewerForeignKey =
    foreignKey("fk_director_movieDirection", reviewerId, ReviewerTable.table) (
      _.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade
    )

  val movieForeignKey =
    foreignKey("fk_movie_movieDirection", movieId, MovieTable.table) (
      _.id, ForeignKeyAction.Cascade, ForeignKeyAction.Cascade
    )
}

object RatingTable{
  val table = TableQuery[RatingTable]
}
