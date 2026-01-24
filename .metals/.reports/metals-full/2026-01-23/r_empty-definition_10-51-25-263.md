error id: file://<WORKSPACE>/src/main/scala/miniEtl/DataLoader.scala:
file://<WORKSPACE>/src/main/scala/miniEtl/DataLoader.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 724
uri: file://<WORKSPACE>/src/main/scala/miniEtl/DataLoader.scala
text:
```scala
package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import scala.io.Source
import scala.util.{Try, Success, Failure}

object class DataLoader{
        def loadRestaurants(filename: String): Either[String, List[Football]] = {
        Try {
            val source = Source.fromFile(filename)
            val content = source.mkString
            source.close()
            content
        } match {
            case Success(content) => 
                decode[List[Restaurant]](content) match {
                    case Right(footabll) => Right(footabll)
                    case Left(error) => Left(s"Parsing error: ${error.getMessage}")
                }
            
            case @@Failure(exception) => Left(s"File error: ${exception.getMessage}")
                }
        
    } 
}


```


#### Short summary: 

empty definition using pc, found symbol in pc: 