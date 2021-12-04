package test;
import java.sql.Time;
import java.util.*;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	float ThresHold = 0.9f;
	List<CorrelatedFeatures> correlatedFeaturesList = new ArrayList<>();

	@Override
	public void learnNormal(TimeSeries ts) {
		Set<String> used = new HashSet<>();
		float max = 0,tmp;
		String header = null;
		for(String k : ts.timeTable.keySet()){
			for(String p:ts.timeTable.keySet()){
				if(used.contains(p) || ts.timeTable.get(k) == ts.timeTable.get(p))
					continue;  //if we already checked the column or if p and k are both the same column move on.
				tmp = Math.abs(StatLib.pearson(vecToFlow(ts.timeTable.get(p)),vecToFlow(ts.timeTable.get(k))));
				if(tmp >= this.ThresHold && tmp > max) {
					max = tmp;
					header = p;
				}
			}
			CorrelatedFeatures correl = new CorrelatedFeatures(k,header,max,lineMaker(ts,k,header),this.ThresHold);
			correlatedFeaturesList.add(correl);
			used.add(k);
			max = 0;
		}

	}

	public float[] vecToFlow(Vector<Float> before){
		int size = before.size();
		float[] after = new float[size];
		for(int i = 0; i < size; i++){
			after[i] = before.get(i);
		}
		return after;
	}

	public CorrelatedFeatures checkCorrel(TimeSeries ts, String correlTo,Set<String> used){
		float max = 0,tmp;
		String header = null;
		for(String k:ts.timeTable.keySet()){
			if(ts.timeTable.keySet().contains(k))
				continue;
			tmp = Math.abs(StatLib.pearson(vecToFlow(ts.timeTable.get(k)),vecToFlow(ts.timeTable.get(correlTo))));
			if(tmp > max) {
				max = tmp;
				header = k;
			}
		}
		CorrelatedFeatures toto = new CorrelatedFeatures(correlTo,header,max,null,this.ThresHold);
		if(max > this.ThresHold)
			return toto;
		return null;
	}

	public Line lineMaker(TimeSeries ts, String a, String b){
		int size = ts.timeTable.get(a).size();
		Point[] points = new Point[size];
		for(int i = 0; i < size; i++){
			points[i] = new Point(ts.timeTable.get(a).get(i),ts.timeTable.get(b).get(i));
		}
		return StatLib.linear_reg(points);
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		return null;
	}
	
	public List<CorrelatedFeatures> getNormalModel(){
		return null;
	}
}
