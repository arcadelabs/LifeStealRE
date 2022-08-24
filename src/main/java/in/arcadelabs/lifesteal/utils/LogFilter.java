package in.arcadelabs.lifesteal.utils;

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