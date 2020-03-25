package NetworkModel;

import java.util.function.Function;

public interface Mappable<T> {
	T map(Function<Double, Double> function);
}
