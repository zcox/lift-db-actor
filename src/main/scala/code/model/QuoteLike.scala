package code.model

import net.liftweb.mapper._
import net.liftweb.common._
import code.actor._

object QuoteLike extends QuoteLike with LongKeyedMetaMapper[QuoteLike] with Logger {
  override def dbTableName = "quote_likes"

  def count(q: Quote): Long = count(By(QuoteLike.quote, q))

  def exists_?(u: User, q: Quote): Boolean = find(u, q) isDefined
  def find(u: User, q: Quote): Box[QuoteLike] = find(By(QuoteLike.user, u), By(QuoteLike.quote, q))

  def like(u: User, q: Quote) = 
    if (!exists_?(u, q)) {
      val ql = QuoteLike.create.user(u).quote(q).saveMe
      debug("User " + u.id.is + " liked Quote " + q.id.is)
      QuotePopularity !<> q
      Full(ql)
    } else 
      Empty

  def unlike(u: User, q: Quote) = 
    for (ql <- find(u, q)) {
      ql.delete_!
      debug("User " + u.id.is + " unliked Quote " + q.id.is)
      QuotePopularity !<> q
    }

  def toggle(u: User, q: Quote) = if (exists_?(u, q)) unlike(u, q) else like(u, q)
}

class QuoteLike extends LongKeyedMapper[QuoteLike] with IdPK with IndexedCreatedUpdated {
  def getSingleton = QuoteLike

  object user extends LongMappedMapper(this, User)
  object quote extends LongMappedMapper(this, Quote)
}
