package dsv.ip.shared;


/**
 * @author Fredrik Sander
 *
 * Interface for event observers.
 */
public interface EventObserver {
  void notify(Event event, Object object);
}
