package NetworkModel;

import java.util.Collection;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Vector1D extends Vector<Double> implements Mappable<Vector1D>{
	private static final long serialVersionUID = 2571706428780109628L;
	
	Vector1D(){
		super();
	}
	Vector1D(Collection<? extends Double> c){
		super(c);
	}
	Vector1D(int initialCapacity){
		super(initialCapacity);
		for(int i = 0; i < initialCapacity; i++) {
			add(0d);
		}
	}
	Vector1D(int initialCapacity, int capacityIncrement){
		super(initialCapacity, capacityIncrement);
		for(int i = 0; i < initialCapacity; i++) {
			add(0d);
		}
	}
	
	public boolean equals(Vector1D other, double threshold) {
		if(other.size() != size()) {
			throw new RuntimeException("Can't compare two vectors if they don't have the same dimensions");
		}
		
		for(int i = 0; i < size(); i++) {
			if(Math.abs(get(i) - other.get(i)) > threshold) return false;
		}
		
		return true;
	}
	
	public Vector1D map(Function<Double, Double> function) {
		return stream().map(function).collect(Collectors.toCollection(Vector1D::new));
	}
	public void destructiveAdd(Vector1D other) {
		for(int i = 0; i < size(); i++){
			set(i, get(i) + other.get(i));
		}
	}
	public Vector1D scale(double scale) {
		return map(ele -> ele * scale);
	}
	public Matrix2D toVertMatrix() {
		Matrix2D vertMatrix = new Matrix2D(size(),1);
		for(int y = 0; y < size(); y++) {
			vertMatrix.storedMatrix[y][0] = get(y);
		}
		return vertMatrix;
	}
	public Matrix2D toHorMatrix() {
		Matrix2D horMatrix = new Matrix2D(1,size());
		for(int x = 0; x < size(); x++) {
			horMatrix.storedMatrix[0][x] = get(x);
		}
		return horMatrix;
	}
	public Vector1D multiplyByWeightMatrix(SynapseWeightMatrix weightMatrix) {
		try {
			return toHorMatrix().matrixMultiply(weightMatrix.getMatrix()).toVector();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Vector1D add(Vector1D currentLayerBiases) {
		if(size() != currentLayerBiases.size()) {
			throw new RuntimeException("Can't elementwise add two vectors if they don't have the same dimensions");
		}
		
		Vector1D result = new Vector1D(this);
		
		for(int i = 0; i < result.size(); i++) {
			result.set(i, get(i)+currentLayerBiases.get(i));
		}
		
		return result;
	}
	public Vector1D sub(Vector1D toSubtract) {
		return add(toSubtract.scale(-1));
	}
	public Vector1D elementMultiply(Vector1D toMultiplyBy) {
		if(size() != toMultiplyBy.size()) {
			throw new RuntimeException("Can't elementwise multiply two vectors if they don't have the same dimensions");
		}
		
		Vector1D result = new Vector1D(this);
		
		for(int i = 0; i < result.size(); i++) {
			result.set(i, get(i)*toMultiplyBy.get(i));
		}
		
		return result;
	}
	public Vector1D elementDivide(Vector1D toDivideBy) {
		if(size() != toDivideBy.size()) {
			throw new RuntimeException("Can't elementwise divide two vectors if they don't have the same dimensions");
		}
		
		Vector1D result = new Vector1D(this);
		
		for(int i = 0; i < result.size(); i++) {
			result.set(i, get(i)/toDivideBy.get(i));
		}
		
		return result;
	}
	
}
