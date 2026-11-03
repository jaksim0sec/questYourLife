package com.questyourlife.app.sync

/** Old unscoped actions are accepted only by the original legacy database. */
object ProfileScope {
    fun permitsAction(expected: String?, active: String) = expected==active || (expected==null && active=="quests.db")
}
