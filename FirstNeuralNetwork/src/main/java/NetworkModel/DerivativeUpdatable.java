package NetworkModel;

public interface DerivativeUpdatable<T> {
	void updateBasedOnDerivative(double learningRate);
	void calculateDerivativesForAll(Vector1D esimateValues, Vector1D expectedValues);
	T getDerivative();
}
