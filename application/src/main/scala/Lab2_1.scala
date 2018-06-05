import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

import datatables._

class Lab2_1(db: Database) {

  /** 1.1 Write a query in Slick to find the name and year of the movies. */
  //mov_title	mov_year
  def task1(implicit ec: ExecutionContext): Future[Set[(String, Int)]] =
    for(res <- db.run(MovieTable.table.map(x=>(x.title, x.year)).result)) yield res.toSet


  /** 1.2. Write a query in Slick to find the year when the movie American Beauty released. */
  def task2(implicit ec: ExecutionContext): Future[Option[Int]] =
    for(res <- db.run(MovieTable.table
      .filter(_.title === "American Beauty")
      .map(_.year).result))
      yield res.headOption

  /** 1.3. Write a query in Slick to find the movies which was released in the year 1999. */
  //mov_title
  def task3(implicit ec: ExecutionContext): Future[Set[String]] =
    for(res <- db.run(MovieTable.table.filter(_.year === 1999).map(_.title).result)) yield res.toSet

  /** 1.4. Write a query in Slick to find the movies which was released before 1998. */
  //mov_title
  def task4(implicit ec: ExecutionContext): Future[Set[String]] =
    for(res <- db.run(MovieTable.table.filter(_.year < 1998).map(_.title).result)) yield res.toSet

  /** 1.5. Write a query in Slick to return the name of all reviewers and name of movies together in a single list. */
  def task5(implicit ec: ExecutionContext): Future[Set[String]] =
    for{
      res1 <- db.run(MovieTable.table.map(_.title).result)
      res2 <- db.run(ReviewerTable.table.map(_.name).result)
    } yield res1.toSet ++ res2.flatten.toSet

  /** 1.6. Write a query in Slick to find the name of all reviewers who have rated 7 or more stars to their rating. */
  def task6(implicit ec: ExecutionContext): Future[Set[String]] =
    for(res <- db.run
    (RatingTable.table
      .join(ReviewerTable.table).on(_.reviewerId === _.id)
      .map(x => (x._1.reviewStars, x._2.name)).result)
    ) yield res.toSet.filter(x => x._1.nonEmpty && x._1.get >= 7 && x._2.nonEmpty).flatMap(_._2)

  /** 1.7. Write a query in Slick to find the titles of all movies that have no ratings. */
  def task7(implicit ec: ExecutionContext): Future[Set[String]] =
    for(res <- db.run
    (MovieTable.table.filterNot(movie => RatingTable.table.filter(_.movieId === movie.id).exists).map(_.title).result)
    ) yield res.toSet

  /** 1.8. Write a query in Slick to find the titles of the movies with ID 905, 907, 917. */
  def task8(implicit ec: ExecutionContext): Future[Set[String]] =
    for(res <- db.run(MovieTable.table.filter(m => m.id === 905 || m.id === 907 || m.id === 917).map(_.title).result))
      yield res.toSet

  /** 1.9. Write a query in Slick to find the list of all those movies with year which include the words Boogie Nights. */
  //mov_id	mov_title	mov_year
  def task9(implicit ec: ExecutionContext): Future[Set[(Int, String, Int)]] =
    for(res <- db.run(MovieTable.table.map(x => (x.id, x.title, x.year)).result))
      yield res.toSet.filter(_._2.contains("Boogie Nights"))

  /** 1.10. Write a query in Slick to find the ID number for the actor whose first name is 'Woody' and the last name is 'Allen'. */
  def task10(implicit ec: ExecutionContext): Future[Option[Int]] =
    for(res <- db.run(ActorTable.table.filter(x=>x.firstName === "Woody" && x.lastName === "Allen").map(_.id).result))
      yield res.headOption
}
