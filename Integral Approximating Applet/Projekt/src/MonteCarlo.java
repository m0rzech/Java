package src;

import java.util.Random;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class MonteCarlo {
		
    public static double countMonteCarloArea(int xStart,int xEnd,XYDataset dataset ,int series,double seed,String fx,double[] y_min_max){
    	
    	int count=dataset.getItemCount(0);
		double n_above=0;
		double n_under=0;
		double ymin=y_min_max[0];
	 	double ymax=y_min_max[1];
		double yrest=0;

		if(ymin>0)
			for(int i =0; i< count; i++){	    		
	    		double x=dataset.getXValue(0, i);    		
	    		double y=dataset.getYValue(0, i);
	    		
	    		double y_funk=Funk.fun(fx, x);
	    		if(isPositive(y_funk) && y<=y_funk && y>=ymin){
	    			n_above++;
	    		}else if(isPositive(y_funk) && y<=y_funk && y>=0)
	    			yrest++;
	    	}
    	else if(ymax<0)
    		for(int i =0; i< count; i++){	    		
	    		double x=dataset.getXValue(0, i);    		
	    		double y=dataset.getYValue(0, i);
	    		
	    		double y_funk=Funk.fun(fx, x);
	    		if((!isPositive(y_funk)) && y>=y_funk && y<=ymax)
	    			n_under++;
	    		else if((!isPositive(y_funk)) && y>=y_funk && y<=0){
	    			yrest++;
	    		}
	    	}
    	else{
    		for(int i=0; i< count; i++){	    		
	    		double x=dataset.getXValue(0, i);    		
	    		double y=dataset.getYValue(0, i);
	    		
	    		double y_funk=Funk.fun(fx, x);
	    		if(isPositive(y_funk) && y<=y_funk && 0<=y){
	    			n_above++;
	    		}else if((!isPositive(y_funk)) && y>=y_funk && 0>=y){
	    			n_under++;
	    		}
	    	}
		}
    	
		
		double S=0;
    	    	
		int a=(xEnd-xStart);
		double P=a*Math.abs(ymax-ymin);	
		
    	if(ymin>0){
    		S=(n_above+n_under)/(count-yrest)*P;
    		S+=a*ymin;
    	}
		else if(ymax<0){    	
    		S=(n_above+n_under)/count*P;
    		S+=a*Math.abs(ymin);
    	}
		else{
	    	S=(n_above+n_under)/count*P;
		}
    	return S;
    }
    

		
	public static XYDataset createDataset(int xStart, int xEnd, String fx,double seed, double dx,int n,double[] y_min_max) {
		 	double ymin=y_min_max[0];
		 	double ymax=y_min_max[1];
		    XYSeries series1 = new XYSeries("First");
		    XYSeries series2 = new XYSeries("");
		    if(ymin>0)
				ymin=0;
			else if(ymax<0)
				ymax=0;
		    		    
		    series2.add(xStart,0);
		    double x,y;
		    for(int i=0;i<n;i++){
		    	
			    if(Math.abs(ymin-ymax)>0){
			    	y=wylosuj(ymin,ymax);
		    	}
		    	else
		    		y=0;
		    		    	
		    	x=wylosuj((double)xStart,(double)xEnd);
		    	series1.add(x,y);
		    }
		
		    final XYSeriesCollection dataset = new XYSeriesCollection();
		    dataset.addSeries(series1);
		    dataset.addSeries(series2);
		    return dataset;
		    
		}
		

	private static double wylosuj(double min,double max){
		    Random r=new Random();
		    
		    double calkowita_max=Math.floor(max-min);
		    double reszta_max=max-min-calkowita_max;		     
		    double reszta=r.nextDouble();
		    double calkowita=r.nextInt(((int)calkowita_max)+1);
		    double s;
		    
			    if(reszta_max!=0.0){
			    	if (calkowita==calkowita_max){
			            while(reszta>reszta_max){
			        		reszta=r.nextDouble();		        		
			        	}
			    	}			    		
			    	s=min+reszta+calkowita;
	        	}else{
	        		reszta=r.nextDouble();      	
	    		    s=min+reszta+r.nextInt((int)Math.floor((max-min)));	
	        	}
		    return s;
	}
	
		
	private static boolean isPositive(double l){
		if(l<=0)
			return false;
		else
			return true;
	}
	
	}
