package com.liuj.huabo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 1. getCounter
 * 2. TimeStats
 */
public class DynamicStats {
	private static Map<String, TimeStats> counterMap = new ConcurrentHashMap<>();
	public static TimeStats getLogCounter(String resourceName){
		TimeStats counter = counterMap.get(resourceName);
		if (counter == null){
			counter = new TimeStats(resourceName);
			counterMap.put(resourceName, counter);
		}
		return counter;
	}

	public static List<TimeStats> getStats(final int minCount){
		List<TimeStats> result = new ArrayList<>();
		for(Map.Entry<String, TimeStats> stats: counterMap.entrySet()){
			if(stats.getValue().getSuccesscount() >= minCount){
				result.add(stats.getValue());
			}
		}
		return result;
	}
	public static void resetStats(List<TimeStats> statsList){
		for(TimeStats counter: statsList){
			counter.reset();
		}
	}
}
