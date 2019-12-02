package SeqrDependencyService.query

import SeqrDependencyService.genomics._

sealed trait OrderingType
case object LessThan extends OrderingType
case object GreaterThan extends OrderingType
case object Equal extends OrderingType

sealed trait QueryBase

case class PositionQuery(order_type: OrderingType, genomic_position: Int, chromosome: HumanChromosome) extends QueryBase
case class InChromosomeQuery(chromosome: HumanChromosome) extends QueryBase