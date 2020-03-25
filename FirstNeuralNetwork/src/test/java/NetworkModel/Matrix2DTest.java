package NetworkModel;

import org.junit.Test;

import junit.framework.Assert;


public class Matrix2DTest {

  @Test
  public void Matrix2Dintint() {
	  int columns, rows;
	  for(int i = 0; i < 100; i++) {
		  columns = 1+(int) (Math.random()*10);
		  rows = 1+(int) (Math.random()*10);
		  
		  Matrix2D testingMatrix = new Matrix2D(rows, columns);
		  
		  for(int y = 0; y < rows; y++) {
			  for(int x = 0; x < columns; x++) {
				  Assert.assertEquals(testingMatrix.storedMatrix[y][x], 0.0f);
			  }
		  }
	  }
  }

  @Test
  public void Matrix2Ddouble() {
	  double[][] doubleArray = 
		  {
				  {0.1f, 0.5f},
				  {0.6f, 0.7f}
		  };
	  double[][] initialisationArray = 
		  {
				  {0.1f, 0.5f},
				  {0.6f, 0.7f}
		  };
	  
	  Matrix2D testingMatrix = new Matrix2D(initialisationArray);
	  
	  //Change the contents to see if it influences the contents of the matrix
	  initialisationArray[0][0] = 1.0f;
	  
	  for(int y = 0; y < doubleArray.length; y++) {
		  for(int x = 0; x < doubleArray[0].length; x++) {
			  Assert.assertEquals(testingMatrix.storedMatrix[y][x], doubleArray[y][x]);
		  }
	  }
	  
  }

  @Test
  public void Matrix2DMatrix2D() {
	  double[][] doubleArray = 
		  {
				  {0.1f, 0.5f},
				  {0.6f, 0.7f}
		  };
	  
	  Matrix2D initialisationMatrix = new Matrix2D(doubleArray);
	  Matrix2D testingMatrix = new Matrix2D(initialisationMatrix);

	  Assert.assertEquals(initialisationMatrix.storedMatrix[0][0], 0.1f);
	  Assert.assertEquals(testingMatrix.storedMatrix[0][0], 0.1f);
	  
	  doubleArray[0][0] = 0.9f;

	  Assert.assertEquals(initialisationMatrix.storedMatrix[0][0], 0.1f);
	  Assert.assertEquals(testingMatrix.storedMatrix[0][0], 0.1f);
	  
	  //Change the original matrix and hope the changes don't propergate over
	  initialisationMatrix.storedMatrix[0][0] = 0.9f;
	  Assert.assertEquals(testingMatrix.storedMatrix[0][0], 0.1f);
  }
  
  @Test
  public void sum() {
    Matrix2D initialMatrix = new Matrix2D.Builder()
    		.addRow(10, 20)
    		.addRow(30, 40)
    		.build();
    
    double resultantMatrix = initialMatrix.sum();
    
    //Make sure the operation was non-destructive
    Assert.assertEquals(initialMatrix.storedMatrix[0][0], 10.0f);

    Assert.assertEquals(resultantMatrix, 100.0f);
  }
  
  @Test
  public void add() {
    Matrix2D initialMatrix = new Matrix2D.Builder()
    		.addRow(10, 20)
    		.addRow(30, 40)
    		.build();
    Matrix2D adderMatrix = new Matrix2D.Builder()
    		.addRow(1, 2)
    		.addRow(3, 4)
    		.build();
    
    Matrix2D resultantMatrix = initialMatrix.add(adderMatrix);
    
    //Make sure the operation was non-destructive
    Assert.assertEquals(initialMatrix.storedMatrix[0][0], 10.0f);
    Assert.assertEquals(adderMatrix.storedMatrix[0][0], 1.0f);
    

    Assert.assertEquals(resultantMatrix.storedMatrix[0][0], 11.0f);
  }

  @Test
  public void columns() {
	  Assert.assertEquals(new Matrix2D.Builder()
	    		.addRow(10, 20)
	    		.addRow(30, 40)
	    		.addRow(50, 60)
	    		.build().columns() , 2);
	  
	  Assert.assertEquals(new Matrix2D.Builder()
	    		.addRow(10)
	    		.addRow(30)
	    		.addRow(50)
	    		.build().columns() , 1);
  }

  @Test
  public void flatten() {
	  double[] flat;
	  
	  Matrix2D test1 = new Matrix2D.Builder()
		.addRow(10, 20)
		.addRow(30, 40)
		.addRow(50, 60)
		.build();
	  
	  flat = test1.flatten();
	  double[] expected1 = {10, 20, 30, 40, 50, 60};
	  
	  Assert.assertEquals(flat, expected1);
	  
	  Matrix2D test2 = new Matrix2D.Builder()
		.addRow(10, 20, 90)
		.addRow(30, 40, 300)
		.build();
	  
	  flat = test2.flatten();
	  double[] expected2 = {10, 20, 90, 30, 40, 300};
	  
	  Assert.assertEquals(flat, expected2);
	  
  }

  @Test
  public void getColumn() {
	  Matrix2D test1 = new Matrix2D.Builder()
				.addRow(10, 20)
				.addRow(30, 40)
				.addRow(50, 60)
				.build();
	  
	  Matrix2D firstColumn = test1.getColumn(0);
	  Assert.assertEquals(firstColumn.storedMatrix[0][0], 10.0f);
	  Assert.assertEquals(firstColumn.storedMatrix[1][0], 30.0f);
	  Assert.assertEquals(firstColumn.storedMatrix[2][0], 50.0f);
	  
	  Matrix2D secondColumn = test1.getColumn(1);
	  Assert.assertEquals(secondColumn.storedMatrix[0][0], 20.0f);
	  Assert.assertEquals(secondColumn.storedMatrix[1][0], 40.0f);
	  Assert.assertEquals(secondColumn.storedMatrix[2][0], 60.0f);
  }

  @Test
  public void getColumns() {
	  Assert.assertEquals(new Matrix2D.Builder()
	    		.addRow(10, 20)
	    		.addRow(30, 40)
	    		.addRow(50, 60)
	    		.build().getColumns() , 2);
	  
	  Assert.assertEquals(new Matrix2D.Builder()
	    		.addRow(10)
	    		.addRow(30)
	    		.addRow(50)
	    		.build().getColumns() , 1);
  }

  @Test
  public void getRow() {
	  Matrix2D test1 = new Matrix2D.Builder()
				.addRow(10, 20)
				.addRow(30, 40)
				.addRow(50, 60)
				.build();
	  
	  Matrix2D firstRow = test1.getRow(0);
	  Assert.assertEquals(firstRow.storedMatrix[0][0], 10.0f);
	  Assert.assertEquals(firstRow.storedMatrix[0][1], 20.0f);
	  
	  Matrix2D secondRow = test1.getRow(1);
	  Assert.assertEquals(secondRow.storedMatrix[0][0], 30.0f);
	  Assert.assertEquals(secondRow.storedMatrix[0][1], 40.0f);
	  
	  Matrix2D thirdRow = test1.getRow(2);
	  Assert.assertEquals(thirdRow.storedMatrix[0][0], 50.0f);
	  Assert.assertEquals(thirdRow.storedMatrix[0][1], 60.0f);
  }

  @Test
  public void getRows() {
	  Assert.assertEquals(new Matrix2D.Builder()
	    		.addRow(10, 20)
	    		.addRow(30, 40)
	    		.addRow(50, 60)
	    		.build().getRows() , 3);
	  
	  Assert.assertEquals(new Matrix2D.Builder()
	    		.addRow(10)
	    		.addRow(30)
	    		.build().getRows() , 2);
  }

  @Test
  public void map() {
	  Matrix2D test1 = new Matrix2D.Builder()
				.addRow(10, 20)
				.addRow(30, 40)
				.addRow(50, 60)
				.build();
	  
	  Matrix2D result = test1.map(ele -> ele/10);
	  
	  //Make sure the operation was non-destructive to the original matrix
	  Assert.assertEquals(test1.storedMatrix[0][0], 10.0f);
	  
	  Assert.assertEquals(result.storedMatrix[0][0], 1.0f);
  }

  @Test
  public void multiply() throws Exception {
	  Matrix2D matrix1 = new Matrix2D.Builder()
				.addRow(10, 20)
				.addRow(30, 40)
				.addRow(50, 60)
				.build();
	  
	  Matrix2D matrix2 = new Matrix2D.Builder()
				.addRow(10, 20)
				.addRow(30, 40)
				.build();
	  
	  Matrix2D result = matrix1.matrixMultiply(matrix2);
	  
	  //Check resulting dimensionality is correct
	  Assert.assertEquals(result.rows(), matrix1.rows());
	  Assert.assertEquals(result.columns(), matrix2.columns());
	  
	  //Check result is correct
	  Assert.assertEquals(result.storedMatrix[0][0], 700.0f);
	  Assert.assertEquals(result.storedMatrix[0][1], 1000.0f);
	  Assert.assertEquals(result.storedMatrix[1][0], 1500.0f);
	  Assert.assertEquals(result.storedMatrix[1][1], 2200.0f);
	  Assert.assertEquals(result.storedMatrix[2][0], 2300.0f);
	  Assert.assertEquals(result.storedMatrix[2][1], 3400.0f);
	  
	  //Check an impossible calculation results in an error
	  try {
		  Matrix2D incomputable = new Matrix2D.Builder()
					.addRow(10, 20)
					.addRow(30, 40)
					.addRow(30, 40)
					.build();
		  
		  matrix1.matrixMultiply(incomputable);
		  
		  Assert.fail();
	  }catch(Exception e) {
		  
	  }
	  
	  //Check that the operation is non-destructive
	  Assert.assertEquals(matrix1.storedMatrix[0][0], 10.0f);
	  Assert.assertEquals(matrix2.storedMatrix[0][0], 10.0f);
  }

  @Test
  public void normaliseVerticallyWith() {
	  Matrix2D matrix1 = new Matrix2D.Builder()
				.addRow(10, 20)
				.addRow(30, 40)
				.addRow(50, 60)
				.build();
	  
	  Matrix2D matrix2 = new Matrix2D.Builder()
				.addRow(100, 200)
				.build();
	  
	  Matrix2D result = matrix1.normaliseVerticallyWith(matrix2);
	  
	  //Check result
	  Assert.assertEquals(result.storedMatrix[0][0], 0.1f);
	  Assert.assertEquals(result.storedMatrix[1][0], 0.3f);
	  Assert.assertEquals(result.storedMatrix[2][0], 0.5f);
	  
	  Assert.assertEquals(result.storedMatrix[0][1], 0.1f);
	  Assert.assertEquals(result.storedMatrix[1][1], 0.2f);
	  Assert.assertEquals(result.storedMatrix[2][1], 0.3f);
	  
	  //Check that the operation is non-destructive
	  Assert.assertEquals(matrix1.storedMatrix[0][0], 10.0f);
	  Assert.assertEquals(matrix2.storedMatrix[0][0], 100.0f);
	  
	  //Check that using an insufficiently small matrix results in an error
	  try {
		  Matrix2D unuseableMatrix = new Matrix2D.Builder()
					.addRow(100)
					.build();
		  
		  matrix1.normaliseVerticallyWith(unuseableMatrix);
		  
		  Assert.fail();
	  }catch(RuntimeException e) {
		  
	  }
	  
	//Check that using a too big matrix is fine
	  try {
		  Matrix2D useableMatrix = new Matrix2D.Builder()
					.addRow(100, 200, 300)
					.build();
		  
		  matrix1.normaliseVerticallyWith(useableMatrix);
		  
	  }catch(RuntimeException e) {
		  Assert.fail();
	  }
  }

  @Test
  public void rows() {
	  Assert.assertEquals(new Matrix2D.Builder()
	    		.addRow(10, 20)
	    		.addRow(30, 40)
	    		.addRow(50, 60)
	    		.build().rows() , 3);
	  
	  Assert.assertEquals(new Matrix2D.Builder()
	    		.addRow(10)
	    		.addRow(30)
	    		.build().rows() , 2);
  }

  @Test
  public void scale() {
	  Matrix2D testingMatrix = new Matrix2D.Builder()
	    		.addRow(10, 20)
	    		.addRow(30, 40)
	    		.addRow(50, 60)
	    		.build();
	  Matrix2D result = testingMatrix.scale(0.1f);
	  
	  Assert.assertEquals(result.storedMatrix[0][0], 1.0f);
	  
	  //Check that the operation is non-destructive
	  Assert.assertEquals(testingMatrix.storedMatrix[0][0], 10.0f);
  }

  @Test
  public void transpose() {
	  Matrix2D testingMatrix = new Matrix2D.Builder()
	    		.addRow(10, 20)
	    		.addRow(30, 40)
	    		.addRow(50, 60)
	    		.build();
	  Matrix2D result = testingMatrix.transpose();
	  
	  Assert.assertEquals(result.rows(), testingMatrix.columns());
	  Assert.assertEquals(result.columns(), testingMatrix.rows());
	  Assert.assertEquals(result.storedMatrix[0][1], 30.0f);
	  Assert.assertEquals(result.storedMatrix[1][0], 20.0f);
	  
	  //Check that the operation is non-destructive
	  Assert.assertEquals(testingMatrix.storedMatrix[0][1], 20.0f);
	  Assert.assertEquals(testingMatrix.storedMatrix[1][0], 30.0f);
  }

  @Test
  public void verticalBatch_AverageVertically() {
	  Matrix2D testingMatrix = new Matrix2D.Builder()
	    		.addRow(10, 20)
	    		.addRow(20, 30)
	    		.addRow(30, 40)
	    		.build();
	  
	  Matrix2D result = testingMatrix.verticalBatch_AverageVertically();
	  
	  Assert.assertEquals(result.rows(), 1);
	  
	  Assert.assertEquals(result.storedMatrix[0][0], 20.0f);
	  Assert.assertEquals(result.storedMatrix[0][1], 30.0f);
	  
	  //Check that the operation is non-destructive
	  Assert.assertEquals(testingMatrix.storedMatrix[0][0], 10.0f);
	  Assert.assertEquals(testingMatrix.storedMatrix[1][0], 20.0f);
  }

  @Test
  public void verticalBatch_ScalarMultiply() {
	  Matrix2D scalarMatrix = new Matrix2D.Builder()
	    		.addRow(2)
	    		.addRow(10)
	    		.addRow(100)
	    		.build();
	  
	  Matrix2D testingMatrix = new Matrix2D.Builder()
	    		.addRow(10, 20)
	    		.addRow(20, 30)
	    		.addRow(30, 40)
	    		.build();
	  
	  Matrix2D result = testingMatrix.verticalBatch_ScalarMultiply(scalarMatrix);
	  
	  Assert.assertEquals(result.rows(), testingMatrix.rows());
	  Assert.assertEquals(result.columns(), testingMatrix.columns());
	  
	  Assert.assertEquals(result.storedMatrix[0][0], 20.0f);
	  Assert.assertEquals(result.storedMatrix[0][1], 40.0f);
	  Assert.assertEquals(result.storedMatrix[1][0], 200.0f);
	  Assert.assertEquals(result.storedMatrix[1][1], 300.0f);
	  Assert.assertEquals(result.storedMatrix[2][0], 3000.0f);
	  Assert.assertEquals(result.storedMatrix[2][1], 4000.0f);
	  
	  //Check that the operation is non-destructive
	  Assert.assertEquals(testingMatrix.storedMatrix[0][0], 10.0f);
	  Assert.assertEquals(testingMatrix.storedMatrix[1][0], 20.0f);
  }

  @Test
  public void verticalBatch_SumVertically() {
	  Matrix2D testingMatrix = new Matrix2D.Builder()
	    		.addRow(10, 20)
	    		.addRow(20, 30)
	    		.addRow(30, 40)
	    		.build();
	  
	  Matrix2D result = testingMatrix.verticalBatch_SumVertically();
	  
	  Assert.assertEquals(result.rows(), 1);
	  
	  Assert.assertEquals(result.storedMatrix[0][0], 60.0f);
	  Assert.assertEquals(result.storedMatrix[0][1], 90.0f);
	  
	  //Check that the operation is non-destructive
	  Assert.assertEquals(testingMatrix.storedMatrix[0][0], 10.0f);
	  Assert.assertEquals(testingMatrix.storedMatrix[1][0], 20.0f);
  }
}
