package code.model

import net.liftweb.mapper._

trait IndexedCreatedUpdated extends CreatedUpdated {
  self: BaseMapper =>

  override protected def createdAtIndexed_? = true
  override protected def updatedAtIndexed_? = true
}
