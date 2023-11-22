package src;

import java.util.Vector;

import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;


public class MidRect {	

	public static XYDataset RectMidDataset( int xStart, int xEnd, String fx,double seed, double dx){
		   
        DefaultXYDataset dataset = new DefaultXYDataset();
        
        Vector<Double> vx=new Vector<Double>();
        Vector<Double> vy=new Vector<Double>();
        Vector<Double> vy2=new Vector<Double>();

        double next=xStart;
        double y =  Funk.fun(fx, xStart+0.5*dx);
        
        for(double x = xStart; x <= xEnd; x+=seed){
        	x=Math.round(x*1000000000.0)/1000000000.0;
            if(x>next){	    	        
            	next+=dx;
            	next=(Math.rint(next*1000000000)/1000000000);
            	
            	//jak nastêpny bok nie mieœci siê w zakresie to za prawy bok uznajemy praw¹ granice zakresu
            	double next2=next-0.5*dx;
            	if(next>xEnd) {
            		next2=(xEnd+next-dx)/2.0;//dzielimy na 2 bo ma byæ œrodek
            		next=xEnd;
            	}
            	if(x!=Math.round((xStart+seed)*1000000000.0)/1000000000.0){
            		x-=seed;
            		x=Math.round(x*1000000000.0)/1000000000.0;
            	}
            	y =  Funk.fun(fx, next2);
            	
            }
            vx.add(x);
            
            
            if ((Double.isNaN(y))||(y==Double.NEGATIVE_INFINITY)||(y==Double.POSITIVE_INFINITY))
            	{
            	vy.add(Double.NaN);
	            vy2.add(Double.NaN);
            	}
            if((!Double.isNaN(y))&&(y!=Double.NEGATIVE_INFINITY)&&(y!=Double.POSITIVE_INFINITY))
            	{
            	vy.add(y);
	            vy2.add(0.0);
	            }
        }
        double[][] data = new double[2][vx.size()];
        double[][] data2 = new double[2][vx.size()];
        Pomocnicze.vecToArray2d(vx,vy,data);
        Pomocnicze.vecToArray2d(vx,vy2,data2);
        
        dataset.addSeries("Integral",data);
        dataset.addSeries("",data2);
        return dataset;
    }
 
	public static double countRectMidArea(XYDataset dataset ,int series,double seed){
    	
    	int count=dataset.getItemCount(0);
		double S=0;    		
		
    	for(int i =0; i< count-1; i++){
    		double x1=dataset.getXValue(0, i);
    		double x2=dataset.getXValue(0, i+1);
    		
    		double y1=dataset.getYValue(0, i);    		
    		
    		if(x1!=x2 && Pomocnicze.isNum(y1)){
    			S=(S+seed*y1);
    		}
    	}
    	return S;
	}
}
