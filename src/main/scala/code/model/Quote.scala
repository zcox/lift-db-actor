package code.model

import net.liftweb.mapper._

object Quote extends Quote with LongKeyedMetaMapper[Quote] {
  override def dbTableName = "quotes"

  def recent(max: Long = 10) = findAll(OrderBy(Quote.createdAt, Descending), MaxRows(max))
  def popular(max: Long = 10) = findAll(By_>(Quote.popularity, 0), OrderBy(Quote.popularity, Descending), OrderBy(Quote.updatedAt, Descending), MaxRows(max))
}

class Quote extends LongKeyedMapper[Quote] with IdPK with IndexedCreatedUpdated {
  def getSingleton = Quote

  object text extends MappedText(this)
  object user extends LongMappedMapper(this, User)
  object popularity extends MappedDouble(this) {
    override def dbIndexed_? = true
  }

  def likeCount = QuoteLike count this
}
