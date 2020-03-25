package NetworkModel.ActivationFunctions;

public enum ActivationFunction {
	
	SigmoidActivation() {
		@Override
		public double getValue(double x) {
			return (1.0/(1.0+Math.exp(-x)));
		}

		@Override
		public double derivativeGetValue(double x) {
			return (Math.exp(-x)/Math.pow(1.0+Math.exp(-x),2));
		}
	},
	//Discovered here: https://www.youtube.com/watch?v=aircAruvnKk - Doesn't work yet
	RectifiedLinearUnitActivation() {
		@Override
		public double getValue(double x) {
			return Math.max(0, x);
		}

		@Override
		public double derivativeGetValue(double x) {
			return x > 0? 1 : 0;
		}
	};
	
	public abstract double getValue(double value);
	public abstract double derivativeGetValue(double value);
}
