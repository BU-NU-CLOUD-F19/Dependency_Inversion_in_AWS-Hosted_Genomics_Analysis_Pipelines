package SeqrDependencyService.wuxi

import scala.util.parsing.json._
import io.lemonlabs.uri._
import requests._
import java.util.Date
import scala.util.Try

class GorConnection(api_key: String, project: String, application_url: String, api_endpoint : String) {

    def get_jwt_token_payload(jwt_token: String) : Either[Map[String,Any],Exception] = {
        var parts = jwt_token.split(".")
        if (parts.length != 3) {
            Right(new IllegalArgumentException("Not a valid JWT token"))
        }
        else {
            var headers = parts(0)
            var payload_raw = parts(1)
    //        var payload = java.util.Base64.getDecoder.decode(payload_raw)
            var signature = parts(2)
            val jsonMap = JSON.parseFull(payload_raw)
            jsonMap match {
                case Some(jsonmapping : Map[String,Any]) => Left(jsonmapping)
                case None => Right(new Exception("Unspecifed error parsing json"))
                case Some(_) => Right(new Exception("Error parsing JSON"))
            } //.values.asInstanceOf[Map[String, Any]]

        }
    }

    def get_access_token(access_endpoint_url : String) : Either[Map[String,String],Exception] =  {
        var tokenparts = get_jwt_token_payload(api_key)
        tokenparts match {
            case Right(error) => Right(error)
            case Left(jsonMapping) =>
                val client_id_raw : Option[Any] = jsonMapping get "azp"
                val client_id = client_id_raw match {
                    case Some(id : String) => id
                    case _ => ""
                }
                val body : Map[String,String] = Map(
                    "client_id" -> client_id,
                    "refresh_token" -> api_key,
                    "grant_type" -> "refresh_token"
                )
                var response = requests.post(
                    access_endpoint_url,
                    params=body
                )
                if (response.statusCode != 200) {
                    Right(new Exception("Web exception when authenticating to GOR: " + response.text))
                }
                else {
                    var try_json_response = JSON.parseFull(response.text)
                    try_json_response match {
                        case None => Right(new Exception("Malformed response in get_access_token, could not parse as JSON"))
                        case Some(mapping : Map[String,Any]) => {
                            val try_tokentype = mapping get "token_type"
                            try_tokentype match {
                                case None => Right(new Exception("Malformed response JSON in get_access_token, did not have token_type field"))
                                case Some(tokentype : String) => {
                                    val try_accesstoken = mapping get "access_token"
                                    try_accesstoken match {
                                        case None => Right(new Exception("Malformed response JSON in get_access_token, did not have token_type field"))
                                        case Some(access_token : String) => {
                                            Left(Map("authorization" -> (tokentype + access_token)))
                                        }
                                        case Some(_) => Right(new Exception("Malformed response JSON in get_access_token, access token was not a string"))
                                }
                            }
                                case Some(_) => {
                                    Right(new Exception("Malformed response JSON in get_access_token, token_type was not a string"))
                                }
                        }
                    }
                        case Some(_) => {
                                    Right(new Exception("Malformed response JSON in get_access_token, token_type was not a string"))
                        }
                }
        }
    }}

    def invoke_api_request(method : String, header: Map[String,String]) : Either[String, Exception] = {
        val resp = method match {
            case "GET" => {
                Left(requests.get(application_url + api_endpoint, params=header))
            }
            case "PUT" => {
                Left(requests.put(application_url + api_endpoint, params=header))
            }
            case "POST" => {
                Left(requests.post(application_url + api_endpoint, params=header))
            }
            case _ => {Right(new Exception("API method " + method + " not supported"))}
        }
        resp match {
            case Right(e) => Right(e)
            case Left(r) => {
                Left(r.text)
            }
        }
    // requests.put(url, )
    }


    def connect() : Either[Map[String,String],Exception] = {
        var token_payload = get_jwt_token_payload(api_key)
        token_payload match {
            case Left(mapping: Map[String,String]) => {
                var expiry_date_str = mapping get "exp"
                expiry_date_str match {
                    case Some(datestr) => {
                        var tryepoch = Try(datestr.toInt).toOption
                        var epoch = {
                            tryepoch match {
                                case Some(epochint:Int) => epochint
                                case None => 0
                            }
                        }
                        var expiry_date = new java.util.Date(epoch*1000L)
                        val now_date = new java.util.Date()
                        if (now_date.compareTo(expiry_date) > 0) {
                            Right(new Exception("JWT token was expired"))
                        }
                        else {
                            var tryurl = mapping get "iss"
                            tryurl match {
                                case None => Right(new Exception("Malformed JWT token did not have ISS field"))
                                case Some(url: String) => {
                                    var tryheader = get_access_token(url + "/protocol/openid-connect/token")
                                    tryheader match {
                                        case Right(e: Exception) => Right(e)
                                        case Left(header : Map[String,String]) => {
                                            val try_access_token_payload = header get "authorization"
                                            try_access_token_payload match {
                                                case None => Right(new Exception("Malformed get_access_token return dict did not have authorization field"))
                                                case Some(payload: String) => {
                                                    val response = invoke_api_request("GET",header)
                                                    response match {
                                                        case Left(m) => {
                                                            Left(Map("OK" -> "ok"))
                                                        }
                                                        case Right(e) => Right(e)
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                                case Some(_) => Right(new Exception("Malformed JWT token"))
                            }

                        }
                    }
                    case None => Right(new Exception("Could not get expiry_date from jwt token payload"))
                }
            }
            case Right(e) => Right(e)
        }
    }
}