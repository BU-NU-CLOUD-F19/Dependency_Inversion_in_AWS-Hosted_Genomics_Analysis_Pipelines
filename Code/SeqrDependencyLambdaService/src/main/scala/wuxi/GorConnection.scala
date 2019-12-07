package seqrdependencylambdaservice.wuxi

import scala.util.parsing.json._
import io.lemonlabs.uri._
import requests._
import java.util.Date
import scala.util.Try

object GorConnection {

    def get_jwt_token_payload(jwt_token: String) : Either[Map[String,String],Exception] = {
        var parts = jwt_token.split(".")
        if (parts.length != 3) {
            Right(new IllegalArgumentException("Not a valid JWT token"))
        }
        else {
            var headers = parts(0)
            var payload_raw = parts(1)
    //        var payload = java.util.Base64.getDecoder.decode(payload_raw)
            var signature = parts(2)
            val jsonMapRaw = JSON.parseFull(payload_raw)
            jsonMapRaw match {
                case Some(mapping : Any) => Left(mapping.asInstanceOf[Map[String,String]])
                case None => Right(new Exception("Unspecifed error parsing json"))
            } //.values.asInstanceOf[Map[String, Any]]

        }
    }

    def create_default() : GorConnection = {
        new GorConnection("","","")
    }
}

case class GorConnection(api_key: String, project: String, application_url: String) {

    var authorizationHeader : Map[String,String] = Map()

    var endpoints : Map[String,String] = Map()

    def get_access_token(access_endpoint_url : String) : Either[Map[String,String],Exception] =  {
        var tokenparts = GorConnection.get_jwt_token_payload(api_key)
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
                        case Some(mappingraw : Any) => {
                            val mapping = mappingraw.asInstanceOf[Map[String,String]]
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
                        }
                    }
                }
        }
    }}

    def merge[K, V](m1:Map[K, V], m2:Map[K, V]):Map[K, V] = {
      (m1.keySet ++ m2.keySet) map { i => i -> (m1.get(i).toList ::: m2.get(i).toList).head } toMap
    }

    def invoke_api_request(method : String, params: Map[String,String], url: String) : Either[Response, Exception] = {
        val resp = method match {
            case "GET" => {
                Left(requests.get(url, params=params, headers=authorizationHeader))
            }
            case "PUT" => {
                Left(requests.put(url, params=params, headers=authorizationHeader))
            }
            case "POST" => {
                Left(requests.post(url, params=params, headers=authorizationHeader))
            }
            case _ => {Right(new Exception("API method " + method + " not supported"))}
        }
        resp
    // requests.put(url, )
    }

    def connect() : Either[Map[String,String],Exception] = {
        var token_payload = GorConnection.get_jwt_token_payload(api_key)
        token_payload match {
            case Left(token_mapping: Map[String,String]) => {
                var expiry_date_str = token_mapping get "exp"
                expiry_date_str match {
                    case Some(datestr : String) => {
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
                            var tryurl = token_mapping get "iss"
                            tryurl match {
                                case None => Right(new Exception("Malformed JWT token did not have ISS field"))
                                case Some(url: String) => {
                                    var tryheader = get_access_token(url + "/protocol/openid-connect/token")
                                    tryheader match {
                                        case Right(e: Exception) => Right(e)
                                        case Left(header : Map[String,String]) => {
                                            authorizationHeader = header
                                            val try_access_token_payload = header get "authorization"
                                            try_access_token_payload match {
                                                case None => Right(new Exception("Malformed get_access_token return dict did not have authorization field"))
                                                case Some(payload: String) => {
                                                    val payloadMapRaw = JSON.parseFull(payload)
                                                    payloadMapRaw match {
                                                        case None => Right(new Exception("Payload in get_access_token could not be parsed as JSON"))
                                                        case Some(payload_mapping: Any) => {
                                                            val payloadMap = payload_mapping.asInstanceOf[Map[String,String]]
                                                            val tryServiceRoot = payloadMap get "service_root"
                                                            tryServiceRoot match {
                                                                case None => Right(new Exception("Could not get service_root from access token"))
                                                                case Some(root: String) => {
                                                                    val connDataNoHeader : Map[String,String] = Map(
                                                                        "api_key" -> api_key,
                                                                        "service_root" -> root,
                                                                        "project" -> project,
                                                                        "api_key_exp" -> ((token_mapping get "exp") match {
                                                                            case Some(exp:String) => exp
                                                                            case None => ""
                                                                        }),
                                                                        "api_key_iat" -> ((token_mapping get "iat") match {
                                                                            case Some(iat:String) => iat
                                                                            case None => ""
                                                                        }),
                                                                        "access_token_exp" -> ((payloadMap get "exp") match {
                                                                            case Some(exp:String) => exp
                                                                            case None => ""
                                                                        }),
                                                                        "access_token_iat" -> ((payloadMap get "iat") match {
                                                                            case Some(iat:String) => iat
                                                                            case None => ""
                                                                        })
                                                                    )
                                                                    val connData = merge(connDataNoHeader, header)
                                                                    var connectResponse = invoke_api_request("GET",connData,root)
                                                                    connectResponse match {
                                                                        case Left(m) => {
                                                                            Left(Map("OK" -> "ok"))
                                                                        }
                                                                        case Right(e: Exception) => Right(e)
                                                                    }
                                                                }
                                                            }
                                                        }
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