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

package in.arcadelabs.lifesteal.hearts;

import in.arcadelabs.lifesteal.LifeStealPlugin;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

/**
 * The type Skull maker.
 *
 * @author Schottky
 * <a href="https://www.spigotmc.org/threads/player-skulls-without-reflection-nms-or-unsafe.458015/">Original code snippet.</a>
 */
@SuppressWarnings("unused")
public class SkullMaker {

  private static final int TYPE_COMPOUND = 10;
  private static final int TYPE_LIST = 9;
  private static final int TYPE_STRING = 8;
  private static final int END_MARK = 0;

  private static void write(DataOutput output, String value) throws IOException {
    output.writeByte(TYPE_COMPOUND);
    output.writeUTF("");

    output.writeByte(TYPE_COMPOUND);
    output.writeUTF("SkullProfile");

    output.writeByte(TYPE_STRING);
    output.writeUTF("Id");
    output.writeUTF(UUID.randomUUID().toString());

    output.writeByte(TYPE_COMPOUND);
    output.writeUTF("Properties");

    output.writeByte(TYPE_LIST);
    output.writeUTF("textures");

    output.writeByte(TYPE_COMPOUND);
    output.writeInt(1);

    output.writeByte(TYPE_STRING);
    output.writeUTF("Value");
    output.writeUTF(value);

    output.writeByte(END_MARK);

    output.writeByte(END_MARK);

    output.writeByte(END_MARK);

    output.writeByte(END_MARK);
  }

  /**
   * Create skull texture.
   *
   * @param value the base64 encoded skull texture value
   * @return the skull texture map
   */
  public Map<String, Object> createSkullMap(String value) {
    try {
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      final DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(out)));
      write(dos, value);
      dos.close();
      final String internal = Base64.getEncoder().encodeToString(out.toByteArray());
      final Map<String, Object> map = new HashMap<>();
      map.put("internal", internal);
      map.put("meta-type", "SKULL");
      map.put("==", "ItemMeta");
      return map;
    } catch (IOException e) {
      LifeStealPlugin.getInstance().getLogger().log(Level.WARNING, "Invalid skull texture value.");
      LifeStealPlugin.getInstance().getLogger().log(Level.WARNING, e.toString());
      return null;
    }
  }
}