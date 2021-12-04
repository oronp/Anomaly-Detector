package test;
import java.sql.Time;
import java.util.*;

public class SimpleAnomalyDetector implements TimeSeriesAnomalyDetector {
	float ThresHold = 0.9f;
	List<CorrelatedFeatures> correlatedFeaturesList = new ArrayList<>();
	List<AnomalyReport> anomalyReports = new ArrayList<>();

	@Override
	public void learnNormal(TimeSeries ts) {
		Set<String> used = new HashSet<>();
		float maxPears = 0,tmp;
		String header = null;
		for(String k : ts.timeTable.keySet()){
			for(String p:ts.timeTable.keySet()){
				if(used.contains(p) || ts.timeTable.get(k) == ts.timeTable.get(p))
					continue;  //if we already checked the column or if p and k are both the same column move on.
				tmp = Math.abs(StatLib.pearson(vecToFlow(ts.timeTable.get(p)),vecToFlow(ts.timeTable.get(k))));
				if(tmp >= this.ThresHold && tmp > maxPears) {
					maxPears = tmp;
					header = p;
				}
			}
			if(maxPears != 0) {
				Point points[] = pointsMaker(ts, k, header);
				Line correntLine = StatLib.linear_reg(points);
				CorrelatedFeatures correl = new CorrelatedFeatures(k, header, maxPears, correntLine, maxDev(correntLine, points));
				correlatedFeaturesList.add(correl);
			}
			used.add(k);
			maxPears = 0;
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

	public Point[] pointsMaker(TimeSeries ts, String a, String b){
		int size = ts.timeTable.get(a).size();
		Point[] points = new Point[size];
		for(int i = 0; i < size; i++){
			points[i] = new Point(ts.timeTable.get(a).get(i),ts.timeTable.get(b).get(i));
		}
		return points;
	}

	public float maxDev(Line line, Point[] points){
		float max = 0,tmp;
		for(int i = 0; i < points.length; i++){
			tmp = StatLib.dev(points[i],line);
			if(tmp > max)
				max = tmp;
		}
		return (1.1f*max);
	}

	@Override
	public List<AnomalyReport> detect(TimeSeries ts) {
		CorrelatedFeatures corrent;
		Point point;
		for(int i = 0; i < correlatedFeaturesList.size(); i++){
			corrent = correlatedFeaturesList.get(i);
			for(int j = 0; j < ts.timeTable.get(corrent.feature1).size(); j++){
				point = new Point(ts.timeTable.get(corrent.feature1).get(i),ts.timeTable.get(corrent.feature2).get(i));
				if(StatLib.dev(point,corrent.lin_reg) > corrent.threshold){
					AnomalyReport anomalyReport = new AnomalyReport(corrent.feature1 + "-" + corrent.feature2,j);
					this.anomalyReports.add(anomalyReport);
				}
			}
		}
		return this.anomalyReports;
	}
	
	public List<CorrelatedFeatures> getNormalModel(){
		return this.correlatedFeaturesList;
	}
}


//	public CorrelatedFeatures checkCorrel(TimeSeries ts, String correlTo,Set<String> used){
//		float max = 0,tmp;
//		String header = null;
//		for(String k:ts.timeTable.keySet()){
//			if(ts.timeTable.keySet().contains(k))
//				continue;
//			tmp = Math.abs(StatLib.pearson(vecToFlow(ts.timeTable.get(k)),vecToFlow(ts.timeTable.get(correlTo))));
//			if(tmp > max) {
//				max = tmp;
//				header = k;
//			}
//		}
//		CorrelatedFeatures toto = new CorrelatedFeatures(correlTo,header,max,null,this.ThresHold);
//		if(max > this.ThresHold)
//			return toto;
//		return null;
//	}

