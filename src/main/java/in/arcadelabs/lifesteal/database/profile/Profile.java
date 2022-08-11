/*
 *          LifeSteal - Yet another lifecore smp core.
 *                Copyright (C) 2022  Arcade Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package in.arcadelabs.lifesteal.database.profile;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import in.arcadelabs.lifesteal.utils.LifeState;
import lombok.Data;

import java.util.UUID;

@Data
@DatabaseTable(tableName = "lifesteal-storage")
public class Profile {

  @DatabaseField(columnName = "UNIQUE_ID", id = true, dataType = DataType.UUID)
  private UUID uniqueID;

  @DatabaseField(columnName = "LIFE_STATE", dataType = DataType.ENUM_TO_STRING)
  private LifeState lifeState;

  @DatabaseField(columnName = "CURRENT_HEARTS")
  private int currentHearts;

  @DatabaseField(columnName = "LOST_HEARTS")
  private int lostHearts;

  @DatabaseField(columnName = "NORMAL_HEARTS")
  private int normalHearts;

  @DatabaseField(columnName = "BLESSED_HEARTS")
  private int blessedHearts;

  @DatabaseField(columnName = "CURSED_HEARTS")
  private int cursedHearts;

  @DatabaseField(columnName = "PEAK_HEARTS_REACHED")
  private int peakHeartsReached;

  // <-- No-args constructor for orm-lite -->
  public Profile() {
  }

  public Profile(UUID uniqueID) {
    this.uniqueID = uniqueID;
  }
}