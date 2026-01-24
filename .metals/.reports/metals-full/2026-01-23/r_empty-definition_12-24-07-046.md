error id: file://<WORKSPACE>/src/main/scala/miniEtl/DataLoader.scala:Left.
file://<WORKSPACE>/src/main/scala/miniEtl/DataLoader.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 756
uri: file://<WORKSPACE>/src/main/scala/miniEtl/DataLoader.scala
text:
```scala
package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import scala.io.Source
import scala.util.{Try, Success, Failure}


object DataLoader{
    def loadFootball(filename: String): Either[String, List[Football]] = {
        Try {
            val = source = Source.fromFile(filename)
            val content = source.mkString
            source.close()
            content
        }
        match {
            case Success(content) => 
                decode[List[RestaurantInt]](content) match {
                    case Right(footballInt) => Right(_.filterValid(isValid()))
                    case Left(error) => Left(s"Parsing error: ${error.getMessage}")
                }
            case Failure(exception) => Lef@@t(s"File error: ${exception.getMessage}")
                }
        }
    }
    


```


#### Short summary: 

empty definition using pc, found symbol in pc: 