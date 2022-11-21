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

package in.arcadelabs.lifesteal.core.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class LogFilter extends AbstractFilter {
  public static void registerFilter() {
    Logger logger = (Logger) LogManager.getRootLogger();
    logger.addFilter(new LogFilter());
  }

  @Override
  public Result filter(final LogEvent event) {
    if (event == null) {
      return Result.NEUTRAL;
    }
    if (event.getLoggerName().contains("Hikari")) {
      return Result.DENY;
    }
    return Result.NEUTRAL;
  }

  @Override
  public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
    return Result.NEUTRAL;
  }

  @Override
  public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
    return Result.NEUTRAL;
  }

  @Override
  public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
    return Result.NEUTRAL;
  }
}
