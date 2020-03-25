package NetworkModel;

import java.util.ArrayList;

public class ResultSet extends ArrayList<Vector1D>{
	private static final long serialVersionUID = 3095594978904284795L;
	
	public String toString() {
		String output = "";
		for(Vector1D vector : this) {
			output += System.lineSeparator() + "| " + vector;
		}
		return output + System.lineSeparator();
	}

	public boolean equals(TrainingData data, double threshold) {
		
		Vector1D myOutputs, otherOutputs;
		for(int ouputSetN = 0; ouputSetN < this.size(); ouputSetN++) {
			myOutputs = get(ouputSetN);
			otherOutputs = data.getNormalisedOutput(ouputSetN);
			
			if(!myOutputs.equals(otherOutputs, threshold)) return false;
		}
		
		return true;
	}
}
