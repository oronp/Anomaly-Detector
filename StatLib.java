package test;
import java.lang.Math;

public class StatLib {

	// simple average
	public static float avg(float[] x){
		float count = 0;
		for(int i = 0; i < x.length; i++){
			count += x[i];
		}
		return (count/(float)x.length);
	}

	// returns the variance of X and Y
	public static float var(float[] x){
		float count1 = 0, count2 = 0;
		float n = 1/(float)x.length;
		for(int i = 0; i < x.length; i++){
			count1 += (float) Math.pow(x[i],2);
			count2 += x[i];
		}
		count1 = n * count1;
		count2 = (float) Math.pow(n*count2,2);
		return (count1 - count2);
	}

	// returns the covariance of X and Y
	public static float cov(float[] x, float[] y){
		float[] xy = new float[x.length];
		for(int i = 0; i < x.length; i++){
			xy[i] = x[i] * y[i];
		}
		return (avg(xy) - avg(x)*avg(y));
	}

	// returns the Pearson correlation coefficient of X and Y
	public static float pearson(float[] x, float[] y){
		return (float)(cov(x,y)/(Math.pow(var(x),0.5)*(Math.pow(var(y),0.5))));
	}

	// performs a linear regression and returns the line equation
	public static Line linear_reg(Point[] points){
		float x[] = new float[points.length];
		float y[] = new float[points.length];
		for(int i = 0; i < points.length; i++){
			x[i] = points[i].x;
			y[i] = points[i].y;
		}
		float a = (cov(x,y) / var(x));
		float b = (avg(y) - (a * avg(x)));
		Line line = new Line(a,b);
		return line;
	}

	// returns the deviation between point p and the line equation of the points
	public static float dev(Point p,Point[] points){
		Line line = linear_reg(points);
		return (dev(p,line));
	}

	// returns the deviation between point p and the line
	public static float dev(Point p,Line l){
		float lineY = l.a*p.x + l.b;
		return Math.abs(lineY - p.y);
	}
	
}
