package globallogicSwing;

import java.util.Comparator;
import java.util.Map;

public class SwingComparator<T1, T2> implements Comparator<Object>{
	
	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object o1, Object o2) {
		Map.Entry<Map<String, Integer>, Integer> a = (Map.Entry<Map<String, Integer>, Integer>) o1;
		Map.Entry<Map<String, Integer>, Integer> b = (Map.Entry<Map<String, Integer>, Integer>) o2;

		int cmp1 = a.getValue().compareTo(b.getValue());
		if(cmp1 != 0) {
			return cmp1;
		} else {
			String key1 = "";
			int value1 = 0;
			for(Map.Entry<String, Integer> entry : a.getKey().entrySet()) {
				key1 = entry.getKey();
				value1 = entry.getValue();
				break;
			}
			
			String key2 = "";
			int value2 = 0;
			for(Map.Entry<String, Integer> entry : b.getKey().entrySet()) {
				key2 = entry.getKey();
				value2 = entry.getValue();
				break;
			}
			
			int cmp2 = Integer.compare(value1, value2);
			if(cmp2 != 0) {
				return cmp2;
			} else {
				return key1.compareTo(key2);
			}
			
		}
	}

}
