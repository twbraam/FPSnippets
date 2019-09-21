///////////////////////////////////////////////////////////////////////
//              alternative constructor for classes                  //

class Rational(x: Int, y: Int) {
  numer = x
  denom = y
}

class Rational(x: Int) {
  new Rational(x, 1)
}
///////////////////////////////////////////////
can also be implemented as:

class Rational(x: Int, y: Int) {
  this(x: Int) = this(x, 1)

  numer = x
  denom = y
}
//                                                                   //
///////////////////////////////////////////////////////////////////////

