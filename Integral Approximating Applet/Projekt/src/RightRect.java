package src;

import java.util.Vector;

import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;


public class RightRect {    	

		public static XYDataset RectRightDataset( int xStart, int xEnd, String fx,double seed, double dx){
			   
	        DefaultXYDataset dataset = new DefaultXYDataset();
	        
	        Vector<Double> vx=new Vector<Double>();
	        Vector<Double> vy=new Vector<Double>();
	        Vector<Double> vy2=new Vector<Double>();

	        double next=xEnd;
	        double y =  Funk.fun(fx, xEnd);
	        	        
	        for(double x = xEnd; x >= xStart; x-=seed){
	        	x=Math.round(x*1000000000.0)/1000000000.0;
	            boolean xprevnum=Pomocnicze.isNum(Funk.fun(fx, Math.round((x-seed)*1000.00)/1000.00));
	            
	            if(x<next){
	            	next-=dx;
	            	next=(Math.rint(next*1000000000)/1000000000);
	            		            	
	            	double next2=Math.round((next+dx)*100.00)/100.00;
	            	if(next<xStart) {
	            		next=xStart;
	            	}
	            	if(x!=Math.round(((xEnd-seed)*100.00)/100.00) && xprevnum){//System.out.println("1.To "+x+"  jest ró¿ne od tego "+xStart+" dlatego w nast it x bedzie t same");
	            		x+=seed;
	            		x=Math.round((x)*100.00)/100.00;
	            	}
	            	y =  Funk.fun(fx, next2);
	            	
	            }
	            vx.add(x);
	            
	            boolean prawy=!Pomocnicze.isNum(y);
	            boolean prawdziwy=!Pomocnicze.isNum(Funk.fun(fx,x));
	            
	            if (prawy & !prawdziwy)
	            {
	            	y =  Funk.fun(fx, x);
	            	vy.add(y);
		            vy2.add(0.0);
	            }
	            else if(prawy || prawdziwy){
	            	vy.add(Double.NaN);
		            vy2.add(Double.NaN);
	            }
	            else
	            {
	            	vy.add(y);
		            vy2.add(0.0);
		        }
	        }
	        double[][] data = new double[2][vx.size()];
	        double[][] data2 = new double[2][vx.size()];
	        Pomocnicze.vecToArray2drev(vx,vy,data);
	        Pomocnicze.vecToArray2drev(vx,vy2,data2);
	        	                
	        dataset.addSeries("Integral",data);      
	        dataset.addSeries("",data2);
	        return dataset;
	    }
	    
	    public static double countRectRightArea(XYDataset dataset ,int series,double seed){
	    	
	    	int count=dataset.getItemCount(0);
    		double S=0;    		
    		
	    	for(int i =0; i< count-1; i++){
	    		double x1=dataset.getXValue(0, i);
	    		double x2=dataset.getXValue(0, i+1);
	    		
	    		double y2=dataset.getYValue(0, i+1);
	    		
	    		
	    		if(x1!=x2 && Pomocnicze.isNum(y2)){
	    			S=(S+seed*y2);
	    		}
	    	}
	    	return S;
	    }
}
