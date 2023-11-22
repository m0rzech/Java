package src;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

//poprawiæ parsery na double i ujemne

public class Funk {
	
	  //funkcja zastepuje x liczba
	  public static String fun_parse_x(String fx,double l){		  
		  String liczba=Double.toString(l);
		  Pattern p=Pattern.compile("(exp)|(x)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		  Matcher m;
		  m = p.matcher(fx);
		  String s = "";
		  StringBuffer sb=new StringBuffer();
		  while (m.find()) //jeœli dopasuje jedn¹ podsekwencjê
		  {
			  if(!m.group().contains("exp")){
				  m.appendReplacement(sb,liczba);
				  //s += m.replaceAll(liczba) ;
			  }
			  else {
				  m.appendReplacement(sb,"exp");
			  }
			  
		  }
		  m.appendTail(sb);
		  s=sb.toString();
		  //System.out.println(s);
		  return s;
	  }
	  
	  //funkcja evaluje wartoœci ale bez znaku potegi
	  public static double eval(String wzor){
		  /*if(wzor.contains("^"))
			  wzor=potegi(wzor);*/
		  Evaluator ev=new Evaluator();	  		  
		  try {
			  //System.out.println("Eval "+wzor);
		  		if(wzor.contains("E"))
		  			wzor=Double.toString(Math.round(Double.parseDouble(wzor)));
			    wzor=ev.evaluate(wzor);	            
	      }
	      catch (EvaluationException e) {
	         //e.printStackTrace();
	    	 // Wykres.fx.setText("Podano nieprawid³owy wzór funkcji, w razie w¹tpliwoœci zjrzeæ do instrukcji");
	      }		  
		  double liczba=Double.parseDouble(wzor);
		  return liczba;
	  }
	  
	  //pobera string z potegami a^b i zastepuje wyewaluowanymi wartoœciami 
	  public static String potegi(String fx){
		  Pattern wzor=Pattern.compile("(-?[0-9.]+)[\"^\"](-?[0-9.]+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		  Matcher matcher_wz = wzor.matcher(fx);
		  String s = "";
	      StringBuffer sb = new StringBuffer();
		  
		  while (matcher_wz.find()) //jeœli dopasuje jedn¹ podsekwencjê a^b
		  {	
			String poprawiony="";
			Matcher matcher_fix =Pattern.compile("(-?[0-9.]+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(matcher_wz.group());
			boolean pierwsza_liczba = true;			
			
			double a=0,b=0;
			while (matcher_fix.find()){		//wynajduje a i b i zamienia na math.
								
				if (pierwsza_liczba) {
					a=Double.parseDouble(matcher_fix.group());
				} else {
					b=Double.parseDouble(matcher_fix.group());
				}
				
				pierwsza_liczba = false;
			}
			a=Math.pow(a, b);
			poprawiony = Double.toString(a);
			
			matcher_wz.appendReplacement(sb, poprawiony);
		  }
		  matcher_wz.appendTail(sb);
		  s=sb.toString();
		  return s;
	  }
	  
	  public static double fun(String fx,double x){
		  if(contains_regex("E",Double.toString(x))){ 
			  x=Math.round(x); 
		  }
		  String s=fun_parse_x(fx,x);
		  s=potegi(s);//System.out.println("  s "+s);
		  //s=potegi2(s);
		  double wynik=eval(s);
		  //double wynik=Double.parseDouble(s);
		  return wynik;
	  }
	  
	  //tu trza jeszcze dodaæ ctg
	  public static String sincos(String fx){
		  Pattern p = Pattern.compile(
				  "(asin[(]([^()]+)[)])|" +
				  "(acos[(]([^()]+)[)])|" +
				  "(sin[(]([^()]+)[)])|" +
				  "(cos[(]([^()]+)[)])|" +
				  "(log[(]([^()]+)[)])|" +
				  "(tg[(]([^()]+)[)])|" +
				  "(ctg[(]([^()]+)[)])|" +
				  "(exp[(]([^()]+)[)])|" +
				  "(atg[(]([^()]+)[)])|" +
				  "(actg[(]([^()]+)[)])");
		  
		  StringBuffer sb = new StringBuffer();
		  
		  while(contains_regex("co", fx)){
			  
			  Matcher m=p.matcher(fx);
			  
			  while (m.find() ) {			  
				  Matcher m2 = Pattern.compile("[0-9+-/*.]+").matcher(m.group());
				  while (m2.find()) {
					  double x;
					  if(contains_regex("[+-/*]+",m2.group())) //chyba nie ma obs³ugi sytuacji jak trafi siê m.group i z tym i z tym 
						  x=eval(m2.group());                         //czyli i znakami dzia³an i E ale to do obadania jeszcze
					  else
						  x=Double.parseDouble(m2.group());

					  if(contains_regex("E",m.group())){
						  x=Math.round(x);
						  }
					  
					  if(m.group().contains("asin")){
						  x=Math.asin(x);
					  }else if(m.group().contains("acos")){
						  x=Math.acos(x);
					  }else if(m.group().contains("log")){
						  x=Math.log(x);
					  }else if(m.group().contains("atg")){
						  x=Math.atan(x);
					  }else if(m.group().contains("actg")){
						  x=Math.tanh(x);
					  }else if(m.group().contains("exp")){
						  x=Math.cos(x);
					  }else if(m.group().contains("sin")){
						  x=Math.sin(x);
					  }else if(m.group().contains("cos")){
						  x=Math.cos(x);
					  }else if(m.group().contains("tg")){
						  x=Math.tan(x);
					  }else if(m.group().contains("ctg")){
						  x=1/Math.tan(x);
					  }
					  
					  if(contains_regex("E",m.group())){
						  x=Math.round(x);
						  }
					  
					  x=Math.round(x*100.0)/100.0;
					  m.appendReplacement(sb,Double.toString(x));					  				  			  
				  }
			  }
			  m.appendTail(sb);
			  fx=sb.toString();
			  sb.delete(0, sb.length());
		  }
		  try{
			  fx=Double.toString(eval(fx));
		  }catch(Exception e){			  
		  }
		  //System.out.println(fx);
		  return fx;
	  }
	  
	  
	  public static boolean contains_regex(String regex, String s){
			 Pattern p=Pattern.compile(regex);
			 Matcher m=p.matcher(s);
			 if(m.find()){
				 //System.out.println(m.group());
				 return true;}
			 else
				 return false;
	  }
	  
	  
	  //public static
	  public static String potegi2(String fx){
		  String wyraz="[()0-9.*+-/a-z]+";
		  Pattern p=Pattern.compile(wyraz+"[\"^\"]"+wyraz, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		  Matcher m = p.matcher(fx);
		  String s = fx;
		  //System.out.println(fx);	
		  StringBuffer sb = new StringBuffer();
		  while(s.contains("^")){
			  //System.out.println("lolo");	
			  while (m.find()) //jeœli dopasuje jedn¹ podsekwencjê a^b
			  {	
				String poprawiony="";
				  System.out.println("pierw w pot2    " +m.group());
				Matcher m2 =Pattern.compile("[^(]([()0-9+*-/a-z&&[^^]])+", Pattern.CASE_INSENSITIVE|Pattern.DOTALL).matcher(m.group());
				boolean pierwsza_liczba = true;		
				
				double a=0,b=0;
				while (m2.find()){		//wynajduje a i b i zamienia na math.
					String tmp=m2.group();	
				    System.out.println("drugi w pot2    " +m2.group());
					if(contains_regex("[a-z]", tmp)){
						tmp=sincos(tmp);
					}
					if(contains_regex("[+-/*]", tmp)){
						tmp=Double.toString(eval(tmp));
					}
					
					if (pierwsza_liczba) {
						a=Double.parseDouble(tmp);
					} else {
						b=Double.parseDouble(tmp);
					}
					
					pierwsza_liczba = false;
				}
				a=Math.pow(a, b);
				poprawiony = Double.toString(a);
				
				m.appendReplacement(sb, poprawiony);
			  }
			  m.appendTail(sb);
			  s=sb.toString();
		  }
		  return s;
	  }

}
