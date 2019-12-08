package seqrdependencylambdaservice.genomics

case class Variant(
    chromosome: HumanChromosome,
    start: Int,
    end: Int,
    ref_allele: Seq[DNABasePair],
    alt_allele: Seq[DNABasePair]
)