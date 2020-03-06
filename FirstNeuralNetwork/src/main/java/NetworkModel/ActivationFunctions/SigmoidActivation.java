package NetworkModel.ActivationFunctions;

public class SigmoidActivation implements ActivationFunction {

	public static void main(String[] args) {
		double val = 0.5d*0.4d;
		System.out.println((new SigmoidActivation()).getValue(val));
		System.out.println((new SigmoidActivation()).derivativeGetValue(val));
	}
	
	@Override
	public double getValue(double x) {
		return (1.0/(1.0+Math.exp(-x)));
	}

	@Override
	public double derivativeGetValue(double x) {
		return (Math.exp(-x)/Math.pow(1.0+Math.exp(-x),2));
	}

}
