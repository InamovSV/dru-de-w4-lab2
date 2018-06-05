import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  def main(args: Array[String]): Unit = {
    val db = Database.forConfig("postgresql")
    val connect = new Lab2_0(db)
    val a = for(r <- SampleData.reviewers) connect.createReviewer(r)
//    val a = db.run(datatables.ActorTable.table.delete)
    val c = for(res <- db.run(datatables.ReviewerTable.table.filter(_.name === "Paul Monks").result)) yield res.toSet
    Thread.sleep(10000)
//    val b = connect.getAllReviewers
    println(a)
    println(Await.result(c, Duration.Inf))
    db.close()
    println("Done!")
  }
}
