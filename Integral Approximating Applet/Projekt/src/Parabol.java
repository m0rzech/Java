package src;

import java.util.Vector;

import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;


public class Parabol {


	
	private static double[][] matrix(double x0,double xi,double ti){
		double[][] A=new double[3][3];
		
		A[0][0]=Math.pow(x0, 2);
		A[0][1]=x0;
		A[0][2]=1;
		A[1][0]=Math.pow(ti, 2);
		A[1][1]=ti;
		A[1][2]=1;
		A[2][0]=Math.pow(xi, 2);
		A[2][1]=xi;
		A[2][2]=1;			
		return A;
	}	
	
	private static double[] vector(double x0,double xi,double ti,String fx){
		double[] b=new double[3];
		b[0]=Funk.fun(fx, x0);
		b[1]=Funk.fun(fx, ti);
		b[2]=Funk.fun(fx, xi);
		return b;
	}
	
	private static double detA(double[][] A){
		for(int i=0;i<=2;i++){
			
		}
		double det=A[0][0]*A[1][1]*A[2][2]+
				   A[0][1]*A[1][2]*A[2][0]+
				   A[0][2]*A[1][0]*A[2][1]-
				   A[0][2]*A[1][1]*A[2][0]-
				   A[0][0]*A[1][2]*A[2][1]-
				   A[0][1]*A[1][0]*A[2][2];
		return det;
	}
	
	private static double[][] zastapkolumne(int j, double[][] A,double[] b){
		double[][] tmp = new double[3][3];
		for(int k=0;k<=2;k++){
			if(k!=j){
				for (int i=0;i<=2;i++){
					tmp[i][k]=A[i][k];
				}
			}else{
				for (int i=0;i<=2;i++){
					tmp[i][j]=b[i];
				}
			}
						
		}
		
		return tmp;
	}
	
	private static String Cramer(double xp,double xk,double t,String fx){
		double[][] A=matrix(xp,xk,t);
		double detA=detA(A);
		double[] B=vector(xp,xk,t,fx);
		
		
		String a=Double.toString(detA(zastapkolumne(0,A,B))/detA);
		String b=Double.toString(detA(zastapkolumne(1,A,B))/detA);
		String c=Double.toString(detA(zastapkolumne(2,A,B))/detA);
		if(a.contains("E"))
  			a=Double.toString(Math.round(Double.parseDouble(a)));
		if(b.contains("E"))
  			b=Double.toString(Math.round(Double.parseDouble(b)));
		if(c.contains("E"))
  			c=Double.toString(Math.round(Double.parseDouble(c)));
		
		
		String Cramer= a+"*pow(x,2)+"+b+"*x+"+c;
		return Cramer;
	}

	
	public static XYDataset ParabolDataset( int xStart, int xEnd, String fx,double seed, double dx){
		   
        DefaultXYDataset dataset = new DefaultXYDataset();
        
        Vector<Double> vx=new Vector<Double>();
        Vector<Double> vy=new Vector<Double>();
        Vector<Double> vy2=new Vector<Double>();
        
        for(double xp=xStart;xp<xEnd;xp=Math.round((xp+dx)*1000000000.0)/1000000000.0){

           	double xk=Math.round((xp+dx)*1000000000.0)/1000000000.0;
        	
        	if (xk>xEnd){
        		xk=xEnd;
        	}
        	
        	double t=Math.round((xk+xp)/2.0*1000000000.0)/1000000000.0;
        	String gx=Cramer(xp,xk,t,fx);
        	
        	for(double x=xp; x <=xk; x+=seed){
        		x=Math.round(x*1000000000.0)/1000000000.0;
        		vx.add(x);
                double y = Funk.fun(gx, x);
                Funk.fun(fx, x);
        		                
                	vy.add(y);
    	            vy2.add(0.0);    	        
        	}
        	
        }       
                
        
        double[][] data = new double[2][vx.size()];
        double[][] data2 = new double[2][vx.size()];
        Pomocnicze.vecToArray2d(vx,vy,data);//wykres pomarañczowy tylko ten bierzemy do liczenia pola
        Pomocnicze.vecToArray2d(vx,vy2,data2);//wykres szary
        
        
        
        dataset.addSeries("Integral",data);      
        dataset.addSeries("",data2);
        return dataset;
    }
	
    public static double countParabolArea(XYDataset dataset ,int series,double seed){
    	
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
