package SeqrDependencyService.query

import SeqrDependencyService.genomics._

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

object GenomicQuery {
    // Synax: "[Variants/SNPs]:[Chromosome=x][PosStart=Y][PosEnd=Z]"
    def fromString(s: String) : (GenomicQuery, GenomicQueryImplementation) = {
        val parts = s.split(",")
        if (parts.length < 2) {
            throw new IllegalArgumentException("Bad query string: " + s)
        }
        else {
            if (parts(1) != "Variants") {
                throw new IllegalArgumentException("Bad query string: " + parts(0))
            }
            else {
                if (parts(2) contains "Chromosome=") {
                    val tryChromosome = HumanChromosome.fromShortString(parts(2).split("Chromosome=")(1))
                    tryChromosome match {
                        case Some(c:HumanChromosome) => {
                            throw new NotImplementedError()
                        }
                    }
                }
                else {
                    throw new IllegalArgumentException("Bad query string: " + parts(0))
                }
            }
        }
    }
}

trait GenomicQueryRepresentation[T] {
    def fromGenomicQuery(q: GenomicQuery)
}