package NetworkModel.ActivationFunctions;

public class RectifiedLinearUnitActivation implements ActivationFunction {
	//Discovered here: https://www.youtube.com/watch?v=aircAruvnKk
	
	@Override
	public double getValue(double x) {
		return Math.max(0, x);
	}

	@Override
	public double derivativeGetValue(double x) {
		return x > 0? 1 : 0;
	}

}
