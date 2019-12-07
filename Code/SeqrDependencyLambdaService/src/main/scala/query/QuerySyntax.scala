package seqrdependencylambdaservice.query

import seqrdependencylambdaservice.genomics._
import scala.util.matching.Regex

sealed trait OrderingType
case object LessThan extends OrderingType
case object GreaterThan extends OrderingType
case object Equal extends OrderingType

sealed trait GenomicQuery

case class PositionQuery(start: Int, end: Int, chromosome: HumanChromosome) extends GenomicQuery
case class InChromosomeQuery(chromosome: HumanChromosome) extends GenomicQuery
case class CombinedQuery(queries: List[GenomicQuery]) extends GenomicQuery

sealed trait GenomicQueryImplementation

case object Wuxi extends GenomicQueryImplementation
case object Hail extends GenomicQueryImplementation
case object TestSecretManager extends GenomicQueryImplementation

object GenomicQuery {
    // Synax: "[Variants/SNPs]:[Chromosome=x][PosStart=Y][PosEnd=Z]:[Hail/Wuxi/TestSecretManager]"
    def fromString(s: String) : Either[(GenomicQuery, GenomicQueryImplementation),String] = {
        val parts = s.split(":")
        if (parts.length != 3) {
            Right("Bad query string: " + s)
        }
        else {
            val queryTypePart = parts(0)
            val queryDetailsPart = parts(1)
            val implementationPart = parts(2)
            val chromosomePattern : Regex = "Chromosome=(\\w+)(?=PosStart)".r
            chromosomePattern.findFirstMatchIn(queryDetailsPart) match {
                case None => Right("Bad query string: " + s)
                case Some(chrStr) => {
                    val tryChromosome = HumanChromosome.fromShortString(chrStr.group(1))
                    tryChromosome match {
                        case Some(c:HumanChromosome) => {
                            // Now that we have the chromosome we hopefully have a valid query.
                            val genomicQuery: Either[GenomicQuery,String] = {
                                val posStartPattern : Regex = "PosStart=(\\d+)".r
                                posStartPattern.findFirstMatchIn(s) match {
                                    case None => {
                                        // this is ok, it's just an InChromosomeQuery
                                        Left(InChromosomeQuery(c))
                                    }
                                    case Some(firstPos) => {
                                        val tryPosStart = try {
                                            Some(firstPos.group(1).toInt)
                                        } catch {
                                            case e : Exception => None
                                        }
                                        tryPosStart match {
                                            case None => Right("Could not get start position from string: " + firstPos.group(1) + "query is" + s)
                                            case Some(i) => {
                                                val posEndPattern : Regex = "PosEnd=(\\d+)".r
                                                posEndPattern.findFirstMatchIn(s) match {
                                                    case None => {
                                                        Right("Start position without an end position not currently supported:" + s)
                                                    }
                                                    case Some(posEnd) => {
                                                        val tryPosEnd = try {
                                                            Some(posEnd.group(1).toInt)
                                                        } catch {
                                                            case e : Exception => None
                                                        }
                                                        tryPosEnd match {
                                                            case None => Right("Could not get end position from string: " + posEnd.group(1) + "query is" + s)
                                                            case Some(j) => {
                                                                Left(PositionQuery(i,j,c))
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                            }
                            }
                            genomicQuery match {
                                case Right(errorString:String) => Right(errorString)
                                case Left(actualQuery: GenomicQuery) => {
                                    implementationPart match {
                                        case "Wuxi" => Left(actualQuery,Wuxi)
                                        case "Hail" => Left(actualQuery,Hail)
                                        case "TestSecretManager" => Left(actualQuery,TestSecretManager)
                                        case _ => Right("Bad query: " + s)
                                    }
                                }
                            }

                        }
                        case None => Right("Bad query: " + s)
                    }
                }
            }
        }
    }
}

trait GenomicQueryRepresentation[T] {
    def fromGenomicQuery(q: GenomicQuery) : T
}