package in.arcadelabs.lifesteal.utils.event;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

@FunctionalInterface
public interface EventsExecutor<E> extends EventExecutor {

    void callEvent(E event);

    @Override
    default void execute(Listener listener, Event event) {
        try {
            callEvent((E) event);
        } catch (final ClassCastException ignored) {}
    }
}
