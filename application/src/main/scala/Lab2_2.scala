import java.time.LocalDate

import model._
import datatables._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class Lab2_2(db: Database) {

  /** 1. Write a query in Slick to list all the information of the actors who played a role in the movie 'Annie Hall'. */
  def task1(implicit ec: ExecutionContext): Future[Set[Actor]] =
    for (res <- db.run
    (MovieCastTable.table
      .join(MovieTable.table.filter(_.title === "Annie Hall")).on(_.movieId === _.id)
      .join(ActorTable.table).on(_._1.actorId === _.id)
      .map(_._2).result))
      yield res.toSet

  /** 2. Write a query in Slick to find the name of the director (first and last names) who directed a movie that casted a role for 'Eyes Wide Shut'. */
  //dir_fname	dir_lname
  def task2(implicit ec: ExecutionContext): Future[Option[(String, String)]] = ???

  /** 3. Write a query in Slick to list all the movies which released in the country other than UK. */
  def task3(implicit ec: ExecutionContext): Future[Set[Movie]] =
    for (res <- db.run(MovieTable.table.filterNot(_.releaseCountry === "UK").result)) yield res.toSet

  /** 4. Write a query in Slick to find the movie title, year, date of release, director and actor for those movies which reviewer is unknown. */
  //mov_title	mov_year	mov_dt_rel	dir_fname	dir_lname	act_fname	act_lname
  def task4(implicit ec: ExecutionContext): Future[Set[(String, Int, LocalDate, String, String, String, String)]] = ???

  /** 5. Write a query in Slick to find the titles of all movies directed by the director whose first and last name are Woody Allen. */
  def task5(implicit ec: ExecutionContext): Future[Set[String]] =
    for (res <- db.run
    (MovieTable.table
      .join(MovieDirectionTable.table
        .join(DirectorTable.table.filter(x => x.firstName === "Woody" && x.lastName === "Allen")).on(_.directorId === _.id))
      .on(_.id === _._1.movieId)
      .map(_._1.title).result
    )) yield res.toSet

  /** 6. Write a query in Slick to find all the years which produced at least one movie and that received a rating of more than 3 stars. Show the results in increasing order. */
  def task6(implicit ec: ExecutionContext): Future[Vector[Int]] = ???

  /** 7. Write a query in Slick to find the titles of all movies that have no ratings. */
  def task7(implicit ec: ExecutionContext): Future[Set[String]] = new Lab2_1(db).task7

  /** 8. Write a query in Slick to find the names of all reviewers who have ratings with a NULL value. */
  def task8(implicit ec: ExecutionContext): Future[Set[String]] =
    for (res <- db.run
    (RatingTable.table
      .join(ReviewerTable.table)
      .on(_.reviewerId === _.id)
      .map(x => (x._2.name, x._1.reviewStars)).result)
    ) yield res.collect {
      case (Some(n), None) => n
    }.toSet

  /** 9. Write a query in Slick to return the reviewer name, movie title, and stars for those movies which reviewed by a reviewer and must be rated. Sort the result by reviewer name, movie title, and number of stars. */
  def task9(implicit ec: ExecutionContext): Future[Vector[(String, String, Double)]] = ???

  /** 10. Write a query in Slick to find the reviewer's name and the title of the movie for those reviewers who rated more than one movies. */
  def task10(implicit ec: ExecutionContext): Future[Set[(String, String)]] =
    for (res <- db.run
    (ReviewerTable.table
      .filter(_.name.nonEmpty)
      .join(RatingTable.table)
      .on(_.id === _.reviewerId)
      .join(MovieTable.table)
      .on(_._2.movieId === _.id)
      .map(x => (x._2.title, x._1._1.name)).result
    )) yield res.groupBy(_._2).filter(x => x._2.size > 1).values.flatten.collect {
      case (title, Some(name)) => (name, title)
    }.toSet

  /** 11. Write a query in Slick to find the movie title, and the highest number of stars that movie received and arranged the result according to the group of a movie and the movie title appear alphabetically in ascending order. */
  def task11(implicit ec: ExecutionContext): Future[Vector[(String, Double)]] = ???

  /** 12. Write a query in Slick to find the names of all reviewers who rated the movie American Beauty. */
  def task12(implicit ec: ExecutionContext): Future[Set[String]] =
    for (res <- db.run
    (ReviewerTable.table
      .join(RatingTable.table.join(MovieTable.table).on(_.movieId === _.id))
      .on(_.id === _._1.reviewerId)
      .filter(_._2._2.title === "American Beauty")
      .map(_._1.name).result
    )) yield res.flatten.toSet

  /** 13. Write a query in Slick to find the titles of all movies which have been reviewed by anybody except by Paul Monks. */
  def task13(implicit ec: ExecutionContext): Future[Set[String]] =
    for (res <- db.run(ReviewerTable.table
      .join(RatingTable.table.join(MovieTable.table).on(_.movieId === _.id))
      .on(_.id === _._1.reviewerId)
      .filterNot(_._1.name.getOrElse("") === "Paul Monks")
      .map(_._2._2.title).result
    )) yield res.toSet

  /** 14. Write a query in Slick to return the reviewer name, movie title, and number of stars for those movies which rating is the lowest one. */
  //rev_name	mov_title	rev_stars
  def task14(implicit ec: ExecutionContext): Future[Option[(String, String, Double)]] =
    for (res <- db.run
    (ReviewerTable.table
      .join(RatingTable.table)
      .on(_.id === _.reviewerId)
      .join(MovieTable.table)
      .on(_._2.movieId === _.id)
      .map(x => (x._1._1.name, x._2.title, x._1._2.reviewStars)).result
    )) yield Option(res.collect{
      case (Some(name), title, Some(stars)) => (name, title, stars)
    }.minBy(_._3))

  /** 15. Write a query in Slick to find the titles of all movies directed by James Cameron. */
  def task15(implicit ec: ExecutionContext): Future[Set[String]] =
    for (res <- db.run
    (DirectorTable.table
      .join(MovieDirectionTable.table.join(MovieTable.table).on(_.movieId === _.id))
      .on(_.id === _._1.directorId)
      .filter(x => x._1.firstName === "James" && x._1.lastName === "Cameron")
      .map(_._2._2.title).result
    )) yield res.toSet

  /** 16. Write a query in Slick to find the name of those movies where one or more actors acted in two or more movies. */
  def task16(implicit ec: ExecutionContext): Future[Set[String]] = ???

}
