package seqrdependencylambdaservice.genomics

// Simple sum type for DNA base pairs
sealed trait DNABasePair
case object Adenine extends DNABasePair
case object Guanine extends DNABasePair
case object Thymine extends DNABasePair
case object Cytosine extends DNABasePair

// Simple sum type for human chromosomes
sealed trait HumanChromosome
case object Chr1 extends HumanChromosome
case object Chr2 extends HumanChromosome
case object Chr3 extends HumanChromosome
case object Chr4 extends HumanChromosome
case object Chr5 extends HumanChromosome
case object Chr6 extends HumanChromosome
case object Chr7 extends HumanChromosome
case object Chr8 extends HumanChromosome
case object Chr9 extends HumanChromosome
case object Chr10 extends HumanChromosome
case object Chr11 extends HumanChromosome
case object Chr12 extends HumanChromosome
case object Chr13 extends HumanChromosome
case object Chr14 extends HumanChromosome
case object Chr15 extends HumanChromosome
case object Chr16 extends HumanChromosome
case object Chr17 extends HumanChromosome
case object Chr18 extends HumanChromosome
case object Chr19 extends HumanChromosome
case object Chr20 extends HumanChromosome
case object Chr21 extends HumanChromosome
case object Chr22 extends HumanChromosome
case object ChrX extends HumanChromosome
case object ChrY extends HumanChromosome
case object ChrM extends HumanChromosome

object HumanChromosome {
    def to_short_string(c: HumanChromosome) = {
        c match {
            case Chr1 => "1"
            case Chr2 => "2"
            case Chr3 => "3"
            case Chr4 => "4"
            case Chr5 => "5"
            case Chr6 => "6"
            case _ => throw new NotImplementedError()
        }
    }

    def to_long_string(c: HumanChromosome) = {
        c match {
            case ChrX => "chrX"
            case ChrY => "chrY"
            case ChrM => "chrM"
            case _ => c.toString().toLowerCase()
        }
    }

    def fromShortString(s: String) : Option[HumanChromosome] = {
        s match {
            case "1" => Some(Chr1)
            case "2" => Some(Chr2)
            case "3" => Some(Chr3)
            case "4" => Some(Chr4)
            case "5" => Some(Chr5)
            case "6" => Some(Chr6)
            case _ => None
        }
    }
}