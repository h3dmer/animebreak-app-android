package com.hedmer.anibreak.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hedmer.anibreak.model.HomeMedia

@Entity(
  tableName = "homeMediaEntity"
)
data class HomeMediaEntity(
  @PrimaryKey val id: Int = FIRST_RECORD_ID,
  val homeMedia: HomeMedia
) {

  companion object {
    const val FIRST_RECORD_ID = 0
  }
}
