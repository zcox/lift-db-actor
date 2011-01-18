package code.actor

import code.model._
import net.liftweb.common._

object QuotePopularity extends DbActor with Logger {
  protected def messageHandler = {
    case q: Quote => 
      val p = q.likeCount
      q.popularity(p).save
      debug("Quote " + q.id.is + " popularity = " + p)
  }
}
