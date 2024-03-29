package seqrdependencylambdaservice.hail

import scala.util.parsing.json._
import seqrdependencylambdaservice.query._
import seqrdependencylambdaservice.genomics._

object HailQuery extends GenomicQueryRepresentation[HailQuery] {
    def fromGenomicQuery(q:GenomicQuery) = {
        var query_string = q match {
            case PositionQuery(start: Int, end: Int, c: HumanChromosome) => {
                "q=locus.contig%3A" + (HumanChromosome.to_short_string(c)) + "%20AND%20locus.position%3A["+start.toString()+"%20TO%20"+end.toString() + "]&pretty"
            }
            case InChromosomeQuery(c: HumanChromosome) => {
                "q=locus.contig%3A" + (HumanChromosome.to_short_string(c)) + "&pretty"
            }
            case CombinedQuery(q: List[GenomicQuery]) => {
                throw new NotImplementedError()
            }
        }
        new HailQuery(query_string)
    }
}

case class HailQuery(queryString: String) {
}