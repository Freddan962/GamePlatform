package dsv.ip.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Fredrik Sander
 *
 * EventSystem is responsible for registering and notifying observing entities.
 */
public class EventSystem {
  public static EventSystem instance = new EventSystem();
  private HashMap<Event, List<EventObserver>> eventRegister = new HashMap<>();
  private EventSystem() { };

  /**
   * Fetches the static instance of the EventSystem.
   * @return The static instance of the EventSystem.
   */
  public static EventSystem getInstance() { return instance; }

  /**
   * Registers a observer for a event.
   * @param event The event to register for.
   * @param observer The observer that will observe the event.
   */
  public void register(Event event, EventObserver observer) {
    if (!eventRegister.containsKey(event))
      eventRegister.put(event, new ArrayList<EventObserver>());

    eventRegister.get(event).add(observer);
  }

  /**
   * Notifies all observers that are listening to a specific event that it occurs.
   * @param event The event that occurred.
   * @param object The optional object containing information regarding the specific event.
   */
  public void notifyEvent(Event event, Object object) {
    if (!eventRegister.containsKey(event))
      return;

    for (EventObserver observer : eventRegister.get(event))
      observer.notify(event, object);
  }
}
