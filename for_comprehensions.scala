///////////////////////////////////////////////////////////////////////
//  assign new values during for-comprehension                       //
val a1, b1, c1 = Option(1)

for {
  i <- a1
  j <- b1
  ij = i + j
  k <- c1
} yield ij + k                                                       //





///////////////////////////////////////////////////////////////////////
//  Either, short-circuit with information (as opposed to Option)   //
val a2, b2 = Right(1)
val c2: Either[String, Int] = Left("sorry, no c")

for {
  i <- a2
  j <- b2
  k <- c2
} yield i + j + k
// > scala.util.Either[String,Int] = Left(sorry, no c)               //
///////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////////////////////
//  Future, short-circuit with future  //
import scala.concurrent._
import ExecutionContext.Implicits.global
for {
  i <- Future.failed[Int](new Throwable)
  j <- Future { println("hello") ; 1 }
} yield i + j
// > scala.concurrent.Future[Int] = Future(Failure(java.lang.Throwable))
///////////////////////////////////////////////////////////////////////




///////////////////////////////////////////////////////////////////////
//  Combine Future with Option using Scalaz' OptionT                 //
import scalaz._, Scalaz._

def getA: Future[Option[Int]] = Future(Some(10))
def getB: Future[Option[Int]] = Future(Some(20))
def getC: Future[Int] = Future(30)
def getD: Option[Int] = Some(1)

val result = for {
  a <- OptionT(getA)
  b <- OptionT(getB)
  c <- getC.liftM[OptionT]
  d <- OptionT(getD.pure[Future])
} yield a * b + c + d
// > result: scalaz.OptionT[scala.concurrent.Future,Int] = OptionT(Future(<not completed>))

// and then get the result with run
result.run
// scala.concurrent.Future[Option[Int]] = Future(Success(Some(200))) //
///////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////////////////////
//  Same as previous but cleaned up using the Scalaz DSL             //
// (the |> (thrush) operator  applies the function on the right to   //
// the value on the left)                                            //

def liftFutureOption[A](f: Future[Option[A]]) = OptionT(f)
def liftFuture[A](f: Future[A]): OptionT[Future, A] = f.liftM[OptionT]
def liftOption[A](o: Option[A]) = OptionT(o.pure[Future])
def lift[A](a: A): OptionT[Future, A] = liftOption(Option(a))


val result = for {
  a <- getA |> liftFutureOption
  b <- getB |> liftFutureOption
  c <- getC |> liftFuture
  d <- getD |> liftOption
  e <- 10 |> lift
} yield e * (a * b) / (c * d)
//                                                                   //
///////////////////////////////////////////////////////////////////////

