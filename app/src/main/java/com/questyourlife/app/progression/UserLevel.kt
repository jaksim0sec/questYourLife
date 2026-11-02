package com.questyourlife.app.progression

data class UserLevel(val level: Long, val totalXp: Long, val currentXp: Long) {
    val remainingXp get() = 100L - currentXp
    val fraction get() = currentXp / 100f

    companion object {
        /** Lifetime completion rewards become XP; purchases never subtract experience. */
        fun fromXp(xp: Long): UserLevel {
            val total = xp.coerceAtLeast(0)
            return UserLevel(total / 100 + 1, total, total % 100)
        }
    }
}
