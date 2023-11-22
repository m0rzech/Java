package src;

import java.util.Vector;

import org.jfree.data.xy.XYDataset;

public class Pomocnicze {
	

	public static double[] getmax(XYDataset ds){
		
		int count=ds.getItemCount(0);		
		double[] y_min_max=new double[2];
		y_min_max[0]=ds.getYValue(0, 0);//y min
		y_min_max[1]=ds.getYValue(0, 0);//y max
		
    	for(int i =1; i< count; i++){
    		double y=ds.getYValue(0, i);    		   		
    	
    		if(y<y_min_max[0]||!Pomocnicze.isNum(y_min_max[0])){
    			y_min_max[0]=y;
    		}
    		if(y>y_min_max[1]||!Pomocnicze.isNum(y_min_max[1])){
    			y_min_max[1]=y;
    		}
    	}
    	return y_min_max;
	}
	  public static boolean isNum(double a){
		  return !((Double.isNaN(a))||(a==Double.NEGATIVE_INFINITY)||(a==Double.POSITIVE_INFINITY));
	  }
	  public static void vecToArray2d(Vector<Double> vx,Vector<Double> vy,double[][] array){
		   for(int i=0;i<vx.size();i++){
			   if((double) vy.get(i)==Double.NEGATIVE_INFINITY||(double) vy.get(i)==Double.POSITIVE_INFINITY||(double) vy.get(i)==Double.NaN){
	           	array[0][i]=(double) vx.get(i);
	           	array[1][i]=(Double) Double.NaN;//System.out.println(" zu");
	           }else{
	           	array[0][i]=(double) vx.get(i);//System.out.println("  x= "+(double) vx.get(i)+"     y = "+(double) vy.get(i));
	           	array[1][i]=(double) vy.get(i);
	           }
		   }
	  }
	  public static void vecToArray2drev(Vector<Double> vx,Vector<Double> vy,double[][] array){
		  int n=vx.size();
		   for(int i=0;i<vx.size();i++){
			   if(!isNum((double) vy.get(n-1-i))){
	           	array[0][i]=(double) vx.get(n-1-i);
	           	array[1][i]=(Double) Double.NaN;
	           }else{
	           	array[0][i]=(double) vx.get(n-1-i);
	           	array[1][i]=(double) vy.get(n-1-i);
	           }
		   }
	  }

}
