package NetworkModel;

import java.util.ArrayList;

public class SpicyArrayList<T> extends ArrayList<T>{
	private static final long serialVersionUID = -4033888576435323557L;
	
	public SpicyArrayList() {};
	public SpicyArrayList(SpicyArrayList<T> toClone) {
		for(int i = 0; i < toClone.size(); i++) {
			add(toClone.get(i));
		}
	}

	public SpicyArrayList<T> tail(){
		SpicyArrayList<T> list = new SpicyArrayList<T>();
		
		for(int i = 1; i < size(); i++) {
			list.add(get(i));
		}
		
		return list;
	}
	public T first() {
		return get(0);
	}
	
}
