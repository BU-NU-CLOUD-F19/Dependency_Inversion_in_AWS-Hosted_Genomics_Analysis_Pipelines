package SeqrDependencyService.wuxi

import scala.util.parsing.json._
import SeqrDependencyService.query._
import SeqrDependencyService.genomics._

class HailQuery(query_string: String) extends GenomicQueryRepresentation[GorQuery] {

    def fromGenomicQuery(q:GenomicQuery) = {
        var query_string = q match {
            case PositionQuery(start: Int, end: Int, c: HumanChromosome) => {
                throw new NotImplementedError()

            }
            case InChromosomeQuery(c: HumanChromosome) => {
                throw new NotImplementedError()
            }
            case CombinedQuery(q: List[GenomicQuery]) => {
                throw new NotImplementedError()
            }
        }
        new HailQuery(query_string)
    }


}