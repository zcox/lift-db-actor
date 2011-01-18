package code.actor

import net.liftweb.actor._
import net.liftweb.mapper._

/** Extend this trait instead of LiftActor directly to have all message handling wrapped in a database transaction. */
trait DbActor extends LiftActor {
  /** Creates the database transaction loan wrapper. If you override aroundLoans in a subclass, you must include this loan wrapper. */
  protected def dbLoanWrapper = DB.buildLoanWrapper
  override protected def aroundLoans = List(dbLoanWrapper)

  /** Sends a message to this actor in DB.performPostCommit block. All messages sent to this actor from inside a database transaction must be sent via this method,
   * to ensure that this actor processes the message after that database transaction has been committed. */
  def !<>(msg: Any): Unit = DB.performPostCommit { this ! msg }
}
