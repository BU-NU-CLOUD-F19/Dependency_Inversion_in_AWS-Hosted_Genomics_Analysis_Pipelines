package SeqrDependencyService.wuxi

import scala.util.parsing.json._
import SeqrDependencyService.query._
import SeqrDependencyService.genomics._

class GorQuery(query_string: String) extends GenomicQueryRepresentation[GorQuery] {

    var query_url : String = ""

    def fromGenomicQuery(q:GenomicQuery) = {
        var query_string = q match {
            case PositionQuery(start: Int, end: Int, c: HumanChromosome) => {
                "gor -p " + HumanChromosome.to_long_string(c) + ":" + (start.toString()) + ":" + (end.toString()) + " #wesVars#"
            }
            case InChromosomeQuery(c: HumanChromosome) => {
                "gor -p " + HumanChromosome.to_long_string(c) + " #wesVars#"
            }
            case CombinedQuery(q: List[GenomicQuery]) => {
                throw new NotImplementedError()
            }
        }
        new GorQuery(query_string)
    }

    def get_query_status(connection: GorConnection) : Either[String,Exception] = {
        if (query_url == "") {
            Left("NOT YET SUBMITTED")
        }
        else {
            var response = connection.invoke_api_request("GET", Map(), query_url)
            response match {
                case Left(data) => {
                    if (data.text contains "FAILED") {
                        Left("FAILED")
                    }
                    else {
                        if (data.text contains "DONE") {
                            Left("DONE")
                        }
                        else {
                            Left("IN PROGRESS")
                        }
                    }
                }
                case Right(e: Exception) => Right(e)
            }

        }
    }

    def get_query_results(connection: GorConnection) {
        val result_url = query_url + "offset=0&limit=10000"
        var tryresponse = connection.invoke_api_request("GET",Map(), result_url)
        tryresponse match {
            case Left(response) => {
                var response_map_raw = JSON.parseFull(response.text)
                response_map_raw match {
                    case None => Right(new Exception("Could not parse JSON result"))
                    case Some(o: Any) => {
                        val response_map = o.asInstanceOf[Map[String,String]]
                        Left(response_map)
                }
            }
        }
            case Right(e) => Right(e)

        }
    }

    def execute_query(connection: GorConnection) : Either[String, Exception] = {
        val body = Map("query" -> query_string, "project" -> connection.project)
        val query_endpoint = (connection.endpoints) get "query"
        query_endpoint match {
            case Some(endpoint) => {
                var query_response = connection.invoke_api_request("POST",body,connection.application_url + query_endpoint)
                Left("OK")
            }
            case None => Right(new Exception("query was not listed in the API endpoints"))
        }

    }
}