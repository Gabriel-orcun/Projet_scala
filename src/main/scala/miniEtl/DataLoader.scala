package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import scala.io.Source
import scala.util.{Try, Success, Failure}


object DataLoader{
    def loadFootball(filename: String): Either[String, List[Football]] = {
        Try {
            val source = Source.fromFile(filename)
            val content = source.mkString
            source.close()
            content
        }
        match {
            case Success(content) => 
                decode[Json](content) match {
                    case Left(error) => Left(s"Parsing error: ${error.getMessage}")
                    case Right(json) => 
                        val listJson = json.asArray.getOrElse(Vector())
                        val players: List[Football] = listJson.flatMap(
                            j => j.as[Football].toOption
                        ).toList
                        Right(players) 
                }
            case Failure(exception) => Left(s"File error: ${exception.getMessage}")
                }
        }
    }
