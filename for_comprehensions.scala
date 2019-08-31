//  assign new values during for-comprehension  //
val a1, b1, c1 = Option(1)

for {
  i <- a1
  j <- b1
  ij = i + j
  k <- c1
} yield ij + k


//  Either, short-circuit with information (as opposed to Option)  //
val a2, b2 = Right(1)
val c2: Either[String, Int] = Left("sorry, no c")

for {
  i <- a2
  j <- b2
  k <- c2
} yield i + j + k
// > scala.util.Either[String,Int] = Left(sorry, no c)



//  Future, short-circuit with future  //
import scala.concurrent._
import ExecutionContext.Implicits.global
for {
  i <- Future.failed[Int](new Throwable)
  j <- Future { println("hello") ; 1 }
} yield i + j
// > scala.concurrent.Future[Int] = Future(Failure(java.lang.Throwable))