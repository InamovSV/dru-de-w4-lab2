import java.time.LocalDate

import datatables.{ActorTable, DirectorTable, GenresTable, MovieCastTable, MovieDirectionTable, MovieGenreTable, MovieTable, RatingTable, ReviewerTable}
import model._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class Lab2_3(db: Database) {
  /** 1. Write a query in Slick to find the name of all reviewers who have rated their ratings with a NULL value.  */
  def task1(implicit ec: ExecutionContext): Future[Set[String]] =
    for (res <- db.run
    (ReviewerTable.table
      .join(RatingTable.table.filter(_.reviewStars.isEmpty))
      .on(_.id === _.reviewerId)
      .map(_._1.name).result)
    ) yield res.flatten.toSet

  /** 2. Write a query in Slick to list the first and last names of all the actors who were cast in the movie 'Annie Hall', and the roles they played in that production. */
  //act_fname	act_lname	role
  def task2(implicit ec: ExecutionContext): Future[Set[(String, String, String)]] =
    for (res <- db.run
    (MovieTable.table
      .filter(_.title === "Annie Hall")
      .join(MovieCastTable.table.join(ActorTable.table).on(_.actorId === _.id))
      .on(_.id === _._1.movieId)
      .map(x => (x._2._2.firstName, x._2._2.lastName, x._2._1.role)).result
    )) yield res.toSet

  /** 3. Write a query in Slick to find the name of movie and director (first and last names) who directed a movie that casted a role for 'Eyes Wide Shut'. */
  //dir_fname	dir_lname
  def task3(implicit ec: ExecutionContext): Future[Option[(String, String)]] = ???

  /** 4. Write a query in Slick to find the name of movie and director (first and last names) who directed a movie that casted a role as Sean Maguire. */
  //dir_fname	dir_lname	mov_title
  def task4(implicit ec: ExecutionContext): Future[Option[(String, String, String)]] =
    for (res <- db.run(MovieTable.table
      .join(MovieCastTable.table.filter(_.role === "Sean Maguire"))
      .on(_.id === _.movieId)
      .join(MovieDirectionTable.table.join(DirectorTable.table).on(_.directorId === _.id))
      .on(_._1.id === _._1.movieId)
      .map(x => (x._2._2.firstName, x._2._2.lastName, x._1._1.title))
      .result
    )) yield res.headOption

  /** 5. Write a query in Slick to list all the actors who acted in a movie before 1990 and also in a movie after 2000. */
  //act_fname	act_lname	mov_title	mov_year
  def task5(implicit ec: ExecutionContext): Future[Set[(String, String, String, Int)]] =
    for (res <- db.run(MovieTable.table
      .filter(x => x.year < 1990 || x.year > 2000)
      .join(MovieCastTable.table.join(ActorTable.table).on(_.actorId === _.id))
      .on(_.id === _._1.movieId)
      .map(x => (x._2._2.firstName, x._2._2.lastName, x._1.title, x._1.year))
      .result
    )) yield res.toSet

  /** 6. Write a query in Slick to list first and last name of all the directors with number of genres movies the directed with genres name, and arranged the result alphabetically with the first and last name of the director. */
  //dir_fname	dir_lname	gen_title	count
  def task6(implicit ec: ExecutionContext): Future[Vector[(String, String, String, Int)]] = ???

  /** 7. Write a query in Slick to list all the movies with year and genres. */
  //mov_title mov_year gen_title
  def task7(implicit ec: ExecutionContext): Future[Set[(String, Int, String)]] =
    for (res <- db.run
    (MovieTable.table
      .join(MovieGenreTable.table.join(GenresTable.table).on(_.genreId === _.id))
      .on(_.id === _._1.movieId)
      .map(x => (x._1.title, x._1.year, x._2._2.title))
      .result
    )) yield res.toSet

  /** 8. Write a query in Slick to list all the movies with year, genres, and name of the director. */
  //mov_title mov_year gen_title dir_fname dir_lname
  def task8(implicit ec: ExecutionContext): Future[Set[(String, Int, String, String, String)]] =
    for (res <- db.run
    (MovieTable.table
      .join(MovieGenreTable.table.join(GenresTable.table).on(_.genreId === _.id))
      .on(_.id === _._1.movieId)
      .join(MovieDirectionTable.table.join(DirectorTable.table).on(_.directorId === _.id))
      .on(_._1.id === _._1.movieId)
      .map(x => (x._1._1.title, x._1._1.year, x._1._2._2.title, x._2._2.firstName, x._2._2.lastName))
      .result)
    ) yield res.toSet

  /** 9. Write a query in Slick to list all the movies with title, year, date of release, movie duration, and first and last name of the director which released before 1st january 1989, and sort the result set according to release date from highest date to lowest. */
  //mov_title mov_year mov_dt_rel mov_time dir_fname dir_lname
  def task9(implicit ec: ExecutionContext): Future[Vector[(String, Int, LocalDate, Int, String, String)]] = ???

  /** 10. Write a query in Slick to compute a report which contain the genres of those movies with their average time and number of movies for each genres. */
  def task10(implicit ec: ExecutionContext): Future[Set[(String, Double, Int)]] = ???

  /** 11. Write a query in Slick to find those lowest duration movies along with the year, director's name, actor's name and his/her role in that production. */
  //mov_title	mov_year	dir_fname	dir_lname	act_fname	act_lname	role
  def task11(implicit ec: ExecutionContext): Future[Option[(String, Int, String, String, String, String, String)]] = ???

  /** 12. Write a query in Slick to find all the years which produced a movie that received a rating of 3 or 4, and sort the result in increasing order. */
  def task12(implicit ec: ExecutionContext): Future[Vector[Int]] =
    for (res <- db.run
    (MovieTable.table
      .join(RatingTable.table.filter(x => x.reviewStars === 3.0 || x.reviewStars === 4.0))
      .on(_.id === _.movieId)
      .map(_._1.year)
      .result
    )) yield res.distinct.sorted.toVector

  /** 13. Write a query in Slick to return the reviewer name, movie title, and stars in an order that reviewer name will come first, then by movie title, and lastly by number of stars. */
  //rev_name	mov_title	rev_stars
  def task13(implicit ec: ExecutionContext): Future[Vector[(String, String, Option[Double])]] = ???

  /** 14. Write a query in Slick to find movie title and number of stars for each movie that has at least one rating and find the highest number of stars that movie received and sort the result by movie title. */
  def task14(implicit ec: ExecutionContext): Future[Vector[(String, Double)]] = ???

  /** 15. Write a query in Slick to find the director's first and last name together with the title of the movie(s) they directed and received the rating. */
  //mov_title	dir_fname	dir_lname	rev_stars
  def task15(implicit ec: ExecutionContext): Future[Set[(String, String, String, Double)]] = ???

  /** 16. Write a query in Slick to find the movie title, actor first and last name, and the role for those movies where one or more actors acted in two or more movies. */
  //mov_title	act_fname	act_lname	role
  def task16(implicit ec: ExecutionContext): Future[Set[(String, String, String, String)]] = ???

  /** 17. Write a query in Slick to find the first and last name of a director and the movie he or she directed, and the actress appeared which first name was Claire and last name was Danes along with her role in that movie. */
  //dir_fname	dir_lname	mov_title	act_fname	act_lname	role
  def task17(implicit ec: ExecutionContext): Future[Set[(String, String, String, String, String, String)]] = ???

  /** 18. Write a query in Slick to find the first and last name of an actor with their role in the movie which was also directed by themselve. */
  //act_fname	act_lname	mov_title	role
  def task18(implicit ec: ExecutionContext): Future[Set[(String, String, String, String)]] = ???

  /** 19. Write a query in Slick to find the cast list for the movie Chinatown. */
  //act_fname	act_lname
  def task19(implicit ec: ExecutionContext): Future[Set[(String, String)]] =
    for (res <- db.run
    (MovieTable.table
      .filter(_.title === "Chinatown")
      .join(MovieCastTable.table.join(ActorTable.table).on(_.actorId === _.id))
      .on(_.id === _._1.movieId)
      .map(x => (x._2._2.firstName, x._2._2.lastName))
      .result
    )) yield res.toSet

  /** 20. Write a query in Slick to find the movie in which the actor appeared whose first and last name are 'Harrison' and 'Ford'. */
  def task20(implicit ec: ExecutionContext): Future[Set[String]] = ???

  /** 21. Write a query in Slick to find the highest-rated movie, and report its title, year, rating, and releasing country. */
  //mov_title	mov_year	rev_stars	mov_rel_country
  def task21(implicit ec: ExecutionContext): Future[Option[(String, Int, Double, String)]] =
    for (res <- db.run
    (MovieTable.table
      .join(RatingTable.table)
      .on(_.id === _.movieId)
      .map(x => (x._1.title, x._1.year, x._2.reviewStars, x._1.releaseCountry))
      .result
    )) yield Option(res.collect{
      case (t, y, Some(s), c) => (t, y, s, c)
    }.maxBy(_._3))

  /** 22. Write a query in Slick to find the highest-rated Mystery movie, and report the title, year, and rating. */
  //mov_title	mov_year	rev_stars
  def task22(implicit ec: ExecutionContext): Future[Option[(String, Int, Double)]] =
    for (res <- db.run(
      GenresTable.table
        .filter(_.title === "Mystery")
        .join(MovieGenreTable.table)
        .on(_.id === _.genreId)
        .join(MovieTable.table.join(RatingTable.table).on(_.id === _.movieId))
        .on(_._2.movieId === _._1.id)
        .map(x => (x._2._1.title, x._2._1.year, x._2._2.reviewStars))
        .result
    )) yield Option(res.collect {
      case (t, y, Some(s)) => (t, y, s)
    }.maxBy(_._3))

  /** 23. Write a query in Slick to generate a report which shows the year when most of the Mystery movies produces, and number of movies and their average rating. */
  //mov_year	gen_title	count	avg
  def task23(implicit ec: ExecutionContext): Future[Option[(Int, String, Int, Double)]] = ???

  /** 24. Write a query in Slick to generate a report which contain the columns movie title, name of the female actor, year of the movie, role, movie genres, the director, date of release, and rating of that movie. */
  //mov_title	act_fname	act_lname	mov_year	role	gen_title	dir_fname	dir_lname	mov_dt_rel	rev_stars
  def task24(implicit ec: ExecutionContext): Future[Set[(String, String, String, Int, String, String, String, String, LocalDate, Double)]] = ???
}
