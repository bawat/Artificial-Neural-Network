package NetworkModel;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Matrix2D {
	public double[][] storedMatrix;
	Matrix2D(int rows, int columns){
		storedMatrix = new double[rows][columns];
	}
	Matrix2D(double[][] matrix){
		storedMatrix = Arrays.stream(matrix).map(double[]::clone).toArray(double[][]::new);
	}
	Matrix2D(Builder builder){
		storedMatrix = builder.storedMatrix;
	}
	Matrix2D(Matrix2D toClone){
		this(toClone.storedMatrix);
	}
	
	public static class Builder{
		private double[][] storedMatrix = new double[0][];
		public Builder addRow(double ...element){
			storedMatrix = Arrays.copyOf(storedMatrix, storedMatrix.length+1);
			storedMatrix[storedMatrix.length-1] = element;
			
			return this;
		}
		
		public Matrix2D build() {
			return new Matrix2D(this);
		}
	}
	
	@Override
	public String toString() {
		return Arrays.deepToString(storedMatrix).replace("], ", "]\n");
	}
	
	public Matrix2D normaliseVerticallyWith(Matrix2D normalisationMatrix){
		Matrix2D result = new Matrix2D(this);
		
		if(normalisationMatrix.columns() < columns()) throw new RuntimeException("Unable to normalise matrix vertically, as the passed normalisationMatrix doesn't have enough columns.");
		
		for(int y = 0; y < rows(); y++) {
			for(int x = 0; x < columns(); x++) {
				result.storedMatrix[y][x] = storedMatrix[y][x]/normalisationMatrix.storedMatrix[0][x];
			}
		}
		
		return result;
	}
	public Matrix2D sub(Matrix2D mat2) {
		return subtract(mat2);
	}
	public Matrix2D subtract(Matrix2D mat2) {
		return this.add(mat2.scale(-1));
	}
	public Matrix2D add(Matrix2D mat2) {
		Matrix2D result = new Matrix2D(this);
		
		for(int y = 0; y < rows(); y++) {
			for(int x = 0; x < columns(); x++) {
				if(y < mat2.rows() && x < mat2.columns())
					result.storedMatrix[y][x] += mat2.storedMatrix[y][x];
			}
		}
		
		return result;
	}
	public Matrix2D matrixMultiply(Matrix2D mat2) throws Exception{
		if(columns() != mat2.rows()) {
			throw new Exception("Incorrect Maxtrix sizes for multiplation");
		}
		
		Matrix2D result = new Matrix2D(rows(),mat2.columns());
		
		for(int resY = 0; resY < result.rows(); resY++) {
			for(int resX = 0; resX < result.columns(); resX++) {
				result.storedMatrix[resY][resX] = elementwiseMultiplyAndAdd(this.getRow(resY), mat2.getColumn(resX));
			}
		}
		
		return result;
	}
	//AKA Hadamard Product
	public Matrix2D elementMultiply(Matrix2D mat2) {
		Matrix2D result = new Matrix2D(mat2);
		
		if(mat2.rows() != rows() || mat2.columns() != columns()) {
			throw new RuntimeException("Can't elementwise multiply two matrices if they don't have the same dimensions");
		}
		
		for(int y = 0; y < rows(); y++) {
			for(int x = 0; x < columns(); x++) {
				result.storedMatrix[y][x] *= storedMatrix[y][x];
			}
		}
		
		return result;
	}
	public Matrix2D transpose() {
		Matrix2D result = new Matrix2D(columns(), rows());
		
		for(int resY = 0; resY < result.rows(); resY++) {
			for(int resX = 0; resX < result.columns(); resX++) {
				result.storedMatrix[resY][resX] = storedMatrix[resX][resY];
			}
		}
		
		return result;
	}
	public Matrix2D empty() {
		return this.map(ele -> 0d);
	}
	private double elementwiseMultiplyAndAdd(Matrix2D matrix1, Matrix2D matrix2) {
		double result = 0;
		
		double[] array1 = matrix1.flatten(), array2 = matrix2.flatten();
		
		for(int i = 0; i < array1.length && i < array2.length; i++) {
			double array1Val = 0, array2Val = 0;
			if(i < array1.length) array1Val = array1[i];
			if(i < array2.length) array2Val = array2[i];
			
			result += array1Val * array2Val;
		}
		
		return result;
	}
	public double[] flatten() {
		double[] flat = new double[columns()*rows()];
		
		for(int y = 0; y < rows(); y++) {
			for(int x = 0; x < columns(); x++) {
				flat[x + y*columns()] = storedMatrix[y][x];
			}
		}
		
		return flat;
	}
	public Matrix2D scale(double multiplier) {
		return this.map(element -> element * multiplier);
	}
	public Matrix2D verticalBatch_ScalarMultiply(Matrix2D otherMatrix) {
		Matrix2D resultMatrix = new Matrix2D(rows(), columns() * otherMatrix.columns());
		
		for(int y = 0; y < rows(); y++) {
			for(int x = 0; x < columns(); x++) {
				for(int otherX = 0; otherX < otherMatrix.columns(); otherX++) {
					resultMatrix.storedMatrix[y][x * otherMatrix.columns() + otherX] = storedMatrix[y][x] * otherMatrix.storedMatrix[y][otherX];
				}
			}
		}
		
		return resultMatrix;
	}
	public Matrix2D verticalBatch_AverageVertically() {
		return verticalBatch_SumVertically().map(ele -> ele/rows());
	}
	public Matrix2D verticalBatch_SumVertically() {
		Matrix2D result = new Matrix2D(1, columns());
		
		for(int y = 0; y < rows(); y++) {
			for(int x = 0; x < columns(); x++) {
				result.storedMatrix[0][x] += storedMatrix[y][x];
			}
		}
		
		return result;
	}
	
	public Matrix2D getRow(int rowN) {
		Matrix2D result = new Matrix2D(1,columns());
		result.storedMatrix[0] = storedMatrix[rowN].clone(); 
		return result;
	}
	public Matrix2D getColumn(int columnN) {
		Matrix2D result = new Matrix2D(rows(),1);
		for(int y = 0; y < rows(); y++) {
			result.storedMatrix[y][0] = storedMatrix[y][columnN];
		}
		
		return result;
	}
	public Matrix2D setRow(int rowN, Matrix2D toSet) {
		Matrix2D result = new Matrix2D(this);
		for(int x = 0; x < columns(); x++) {
			result.storedMatrix[rowN] = toSet.storedMatrix[0].clone();
		}
		
		return result;
	}
	public Matrix2D setColumn(int columnN, Matrix2D toSet) {
		Matrix2D result = new Matrix2D(this);
		for(int y = 0; y < rows(); y++) {
			result.storedMatrix[y][columnN] = toSet.storedMatrix[y][0];
		}
		
		return result;
	}
	
	public int rows() {
		return storedMatrix.length;
	}
	public int getRows(){return rows();}
	public int columns() {
		return storedMatrix[0].length;
	}
	public int getColumns(){return columns();}
	
	public Matrix2D map(Function<Double, Double> funct) {
		Matrix2D result = new Matrix2D(rows(),columns());
		
		for(int y = 0; y < rows(); y++) {
			for(int x = 0; x < columns(); x++) {
				result.storedMatrix[y][x] = (double) funct.apply(storedMatrix[y][x]);
			}
		}
		
		return result;
	}
	public void destructiveUpdate(Matrix2D other) {
		storedMatrix = add(other).storedMatrix;
	}
	public Matrix2D weirdEndThingAndAverageVertically(Matrix2D x) throws Exception {
		Matrix2D result = new Matrix2D(x.rows(),x.columns());
		
		for(int i = 0; i < rows(); i++) {
			Matrix2D inputA = this.getRow(i);
			Matrix2D inputB = x.getRow(i);
			
			result = result.add(inputA.transpose().matrixMultiply(inputB));
		}
		
		return result.map(ele -> ele/rows());
	}
	public double sum() {
		double result = 0;
		
		for(int y = 0; y < rows(); y++) {
			for(int x = 0; x < columns(); x++) {
				result += storedMatrix[y][x];
			}
		}
		
		return result;
	}
	public double val() {
		return storedMatrix[0][0];
	}
	public boolean equals(Matrix2D otherMatrix, double threshold) {
		if(otherMatrix.rows() != rows() || otherMatrix.columns() != columns()) {
			throw new RuntimeException("Can't compare two matrices if they don't have the same dimensions");
		}
		
		for(int y = 0; y < rows(); y++) {
			for(int x = 0; x < columns(); x++) {
				if(Math.abs(storedMatrix[y][x] - otherMatrix.storedMatrix[y][x]) > threshold) return false;
			}
		}
		
		return true;
	}
	public Matrix2D stretchVertically(int rows) {
		Matrix2D result = new Matrix2D(rows,columns());
		
		for(int row = 0; row < result.rows(); row++) {
			result.storedMatrix[row] = storedMatrix[0].clone();
		}
		
		return result;
	}
	
}
