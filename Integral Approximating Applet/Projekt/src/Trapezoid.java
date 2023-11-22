package src;

import java.util.Vector;

import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class Trapezoid {
	public static XYDataset TrapezoidDataset( int xStart, int xEnd, String fx,double seed, double dx){
		   
        DefaultXYDataset dataset = new DefaultXYDataset();
        
        Vector<Double> vx=new Vector<Double>();
        Vector<Double> vy=new Vector<Double>();
        Vector<Double> vy2=new Vector<Double>();

        double next=xStart;
        double y =  Funk.fun(fx, xStart);
        
        
        for(double x = xStart; x <= xEnd; x+=seed){
        	x=Math.round(x*1000000000.0)/1000000000.0;
        	
            if(x>next || x==xEnd  ){    
            	next+=dx;
            	next=(Math.rint(next*1000000000)/1000000000);            	
            	
            	//jak nastêpny bok nie mieœci siê w zakresie to za prawy bok uznajemy praw¹ granice zakresu
            	double next2=Math.round((next-dx)*100.00)/100.00;
            	if(next>xEnd) {
            		next=xEnd;
            	}
            	
            	//sprawdzanie czy nie jest to poczatek przedzialu, lub poczatek wykresu(po ciagach wart nienumerycznych)            	
	            if(x!=xStart+seed  && x!=xEnd){
	            	x-=seed;
	            }
	            
            	y =  Funk.fun(fx, next2);        	
            
            	boolean lewy=!Pomocnicze.isNum(y);
	            boolean prawdziwy=!Pomocnicze.isNum(Funk.fun(fx,x));
	            
	            if (lewy & !prawdziwy)
	            {
	            	y =  Funk.fun(fx, x);
	            	vy.add(y);vx.add(x);
		            vy2.add(0.0);
	            }
	            else
	            {
	            	vy.add(y);
		            vy2.add(0.0);vx.add(x);
		        }
	            
            }else if(x==xStart){
            	
            	boolean lewy=!Pomocnicze.isNum(y);
	            boolean prawdziwy=!Pomocnicze.isNum(Funk.fun(fx,x));
	            
	            if (lewy & !prawdziwy)
	            {
	            	y =  Funk.fun(fx, x);
	            	vy.add(y);vx.add(x);
		            vy2.add(0.0);
	            }
	            else
	            {
	            	vy.add(y);
		            vy2.add(0.0);vx.add(x);
		        }
	            
	            next+=dx;
            	next=(Math.rint(next*1000000000)/1000000000);
            	
            	if(next>xEnd) {
            		next=xEnd;
            	}
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
		
	
    public static double countTrapezoidArea(XYDataset dataset ,int series,double seed){
    	
    	int count=dataset.getItemCount(0);
		double S=0;    		
		
    	for(int i =0; i<= count-2; i++){
    		double x1=dataset.getXValue(0, i);
    		double x2=dataset.getXValue(0, i+1);
    		double h=Math.round((x2-x1)*100000000.00)/100000000.00;
    		
    		double y1=dataset.getYValue(0, i);
    		double y2=dataset.getYValue(0, i+1);
    		
    		
    		if(x1!=x2 && Pomocnicze.isNum(y1)){
    			S=(S+0.5*h*Math.round((y1+y2)*100000000.00)/100000000.00);
    			
    		}
    	}
    	return S;
    }
}
