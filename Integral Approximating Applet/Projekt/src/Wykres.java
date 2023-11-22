package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDifferenceRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class Wykres extends java.applet.Applet {

	public void init() {
		setSize(1250, 500);
		new Wykres();
	}
	//log(7 * x + 7) + cos(7 * x)
	private String funkcja = "log(x *37+0.01) + cos(7 / (x+0.01))";
	private int a = 0, b = 5;
	private double dxwart = 0.5, seed = 0.005;
	public int method = 0;
	XYLineAndShapeRenderer render = new XYLineAndShapeRenderer();
	boolean ymn=false;
	boolean ymx=false;
	boolean hasNaN=false;
	String ZakresNaN="";

	public JTextField fx = new JTextField(funkcja, 20);
	JTextField ax = new JTextField(Integer.toString(a), 20), bx = new JTextField(Integer.toString(b),
			20), dx = new JTextField("0.5", 10),
			ena = new JTextField("10", 10);

	JLabel area = new JLabel("Obszar:"), n = new JLabel("(liczba punkt�w) n:"),
			realarea = new JLabel("Prawdziwy Obszar:"),error = new JLabel("B��d:"),ymin = new JLabel(),ymax = new JLabel();
	

	double[] y_min_max;

	JButton ok = new JButton("rysuj wykres"), plus = new JButton(" + 1"),
			minus = new JButton(" - 1"),yminbutton = new JButton("y min"),ymaxbutton = new JButton("y max");

	JRadioButton Middle = new JRadioButton("�rodkowegoPunktu"), Left = new JRadioButton(
			"LewegoPunktu"), Right = new JRadioButton("PrawegoPunktu"),
			Trapez = new JRadioButton("Trapez�w"), Simpson = new JRadioButton(
					"Simpsona"), Monte = new JRadioButton("Monte Carlo");

	JSlider dxe = new JSlider(JSlider.HORIZONTAL, 0, 19, 10);

	JTabbedPane tab1 = new JTabbedPane();

	JPanel panel1 = new JPanel(); // definiujeny panel1
		
	XYDataset dataset1,dataset2;
	
	XYPlot plot;	

	JFreeChart chart;
	
	ChartPanel myChart;

	XYItemRenderer renderer=new XYDifferenceRenderer(Color.orange,Color.ORANGE, false);
	XYItemRenderer renderer2=new XYDifferenceRenderer(Color.orange,Color.ORANGE, false);
	
	Marker m1,m2;

	// ===================================================
	@SuppressWarnings("deprecation")
	public Wykres() {

		dxe.setMajorTickSpacing(20);
		dxe.setMinorTickSpacing(1);
		dxe.setPaintTicks(true);
		
		// kolor przycisku ok
		yminbutton.setBackground(Color.lightGray);
		ymaxbutton.setBackground(Color.lightGray);

		// opcje slidera
		dxe.addChangeListener(new Adx());

		// w�a�ciwo�ci panelu
		panel1.setLayout(new GridBagLayout());

		
		Box b1 = Box.createVerticalBox();
		b1.add(new JLabel("Funkcja:"));
		b1.add(new JLabel("(pocz�tek przedzia�u) a:"));
		b1.add(new JLabel("(koniec przedzia�u) b:"));
		n.enable(false);
		b1.add(n);

		Box b2 = Box.createVerticalBox();
		b2.add(fx);
		b2.add(ax);
		b2.add(bx);
		ena.enable(false);
		b2.add(ena);

		Box box1 = Box.createHorizontalBox();
		box1.add(b1);
		box1.add(b2);

		ok.addActionListener(new Aok());
		plus.addActionListener(new Aplus());
		minus.addActionListener(new Aminus());
		yminbutton.addActionListener(new Aymin());
		ymaxbutton.addActionListener(new Aymax());

		
		dataset1 = createDataset(a, b, funkcja, 0.001);
		dataset2 = MidRect.RectMidDataset(a, b, funkcja, seed, dxwart);
		double areaval=round(MidRect.countRectMidArea(dataset2, 0, seed));
		area.setText("Obszar:   "+ Double.toString(areaval));
		
		ValueAxis xAxis = new NumberAxis("x");
		ValueAxis yAxis = new NumberAxis("y");

		renderer.setSeriesPaint(0, Color.cyan); 
		renderer2.setSeriesPaint(0, Color.orange); 
		renderer.setSeriesPaint(1, Color.cyan); 												// nachodzacy na pierwszy
		renderer2.setSeriesPaint(1, Color.gray); 
		renderer.setSeriesVisibleInLegend(1, false);
		renderer2.setSeriesVisibleInLegend(1, false);
				
		plot = new XYPlot(dataset1, xAxis, yAxis, renderer);
		plot.setDataset(1, dataset2);
		plot.setRenderer(1, renderer2);		
		
		plot.addRangeMarker(new ValueMarker(0));
		chart = new JFreeChart(
				"Aproksymacja ca�ki oznaczonej za pomoc� metod numerycznych",
				new Font("Tahoma", 2, 18), plot, true);
		

		myChart = new ChartPanel(chart, true, false, false, true, true);
		myChart.scrollRectToVisible(getBounds());
		
		// nastepne menu do wyboru beda w boxach
		addItem(panel1, myChart, 1, 0, 1, 1, GridBagConstraints.EAST);
		Box c1=Box.createHorizontalBox();
		c1.add(createTrace());
		c1.add(Box.createRigidArea(new Dimension(30,5)));
		c1.add(yminbutton);
		c1.add(Box.createRigidArea(new Dimension(30,5)));
		c1.add(ymaxbutton);
		c1.add(Box.createRigidArea(new Dimension(270,5)));
		addItem(panel1, c1, 1, 1, 1, 1, GridBagConstraints.EAST);

		Box sizeBox = Box.createVerticalBox();
		ButtonGroup sizeGroup = new ButtonGroup();
		sizeGroup.add(Middle);
		sizeGroup.add(Left);
		sizeGroup.add(Right);
		sizeGroup.add(Trapez);
		sizeGroup.add(Simpson);
		sizeGroup.add(Monte);
		sizeBox.add(Middle);
		sizeBox.add(Left);
		sizeBox.add(Right);
		sizeBox.add(Trapez);
		sizeBox.add(Simpson);
		sizeBox.add(Monte);
		sizeBox.setBorder(BorderFactory.createTitledBorder("Metoda"));
		
		/*********************************************************/
		Box exampl = Box.createVerticalBox();
		exampl.add(createexamples());
		exampl.setBorder(BorderFactory.createTitledBorder("Przyk�ady"));

		/* listenery do radiobutton�w */

		Middle.addActionListener(new Amid());
		Left.addActionListener(new Aleft());
		Right.addActionListener(new Aright());
		Trapez.addActionListener(new Atrap());
		Simpson.addActionListener(new Asimp());
		Monte.addActionListener(new Amc());

		// ustalamy pierwszy algorytm za wybrany
		Middle.setSelected(true);

		/*
		 * box2 w kt�rym znajduj� sie przyciski i poletextowe odpowiadaj�ce za
		 * parametr dx
		 */
		Box box2 = Box.createHorizontalBox();
		box2.add(minus);
		box2.add(dx);
		box2.add(plus);
		/* box3 box w kt�rym znajduje sie box1 i slider ustalaj�cy paraetr dx */
		Box box3 = Box.createVerticalBox();
		box3.add(box2);
		box3.add(dxe);
		box3.setBorder(BorderFactory.createTitledBorder("dx"));

		Box box0 = Box.createVerticalBox();
		Box ex_gl = Box.createVerticalBox();
		Box alg_exmp = Box.createHorizontalBox();
		box0.add(box1);
		box0.add(box2);
		box0.add(box3);
		alg_exmp.add(sizeBox);
		ex_gl.add(exampl);
		ex_gl.add(Box.createRigidArea(new Dimension(70,10)));
		ex_gl.add(new JSeparator());
		Box a1=Box.createHorizontalBox();
		a1.add(ok);
		a1.add(Box.createRigidArea(new Dimension(120,10)));
		ex_gl.add(a1);
		ex_gl.add(Box.createRigidArea(new Dimension(70,40)));
		alg_exmp.add(ex_gl);
		box0.add(alg_exmp);
		box0.add(new JSeparator());
		Box a2=Box.createHorizontalBox();
		a2.add(area);
		a2.add(Box.createRigidArea(new Dimension(0,5)));
		box0.add(a2);
		box0.add(new JSeparator());
		Box a3=Box.createHorizontalBox();
		a3.add(realarea);
		a3.add(Box.createRigidArea(new Dimension(0,5)));
		box0.add(a3);
		box0.add(new JSeparator());
		Box a4=Box.createHorizontalBox();
		a4.add(error);
		a4.add(Box.createRigidArea(new Dimension(0,5)));
		box0.add(a4);
		box0.add(new JSeparator());
		Box a5=Box.createHorizontalBox();
		a5.add(ymin);
		a5.add(Box.createRigidArea(new Dimension(0,5)));
		box0.add(a5);
		box0.add(new JSeparator());
		Box a6=Box.createHorizontalBox();
		a6.add(ymax);
		a6.add(Box.createRigidArea(new Dimension(0,5)));
		box0.add(a6);
				
		Box box = Box.createVerticalBox();
		box.add(box0);
		tab1.add(box, "Dane Wykresu");
		addItem(panel1, tab1, 0, 0, 1, 1, GridBagConstraints.NORTH);

		Box tbox = Box.createVerticalBox();
		JTextArea t = new JTextArea(
				"1.Przeznacznie Apletu"
						+ "  Aplet s�u�y ilustrowania oszacowa� warto�ci ca�ki oznaczonej za pomoc� wybranych metod."
						+ " Pozwala prze�ledzi� r�nice w obliczeniach dla r�nych algorytm�w. "
						+ " Istnieje mo�liwo�� podania w�asnych danych, b�d� wczytania gotowych."
						+ " przyk��d�w. \n"
						+ " \n"
						+ "2.Dane uwzgl�dniane w obliczeniach obejmuj�: \n"
						+ "    *a � pocz�tek przedzia�u\n"
						+ "    *b � koniec przedzia�u ca�kowania\n"
						+ "    *n � liczba losowanych punkt�w (MonteCarlo)\n"
						+ "    *dx � d�ugo�� podprzedzia��w ca�kowania; \n"
						+ "     d�ugo�� podstawy ka�dego sumowanego wielok�ta(Sumy\n    Riemanna) \n"
						+ "     *algorytm wykorzystywany w obliczeniach obszaru pod\n    wykresem\n"
						+ " \n"
						+ "3.Zasady korzystania\n"
						+ "Aby unikn�� podania b��dnych danych ale�y stosowa� si� do zasad: \n"
						+ "   *warto�� pocz�tku przedzia�u (a) powinna by� mniejsza od\n    warto�ci ko�ca przedzia�u (b)\n"
						+ "   *funkcja powinna by� ca�kowalna na zadanym zakresie\n"
						+ "   *wsp�rz�dne granic zakresu powinny by� podane w\n    formacie liczb ca�kowitych\n"
						+ "   *aby program dzia�a� optymalnie zakres nie powinien\n    przekracza� 100\n"
						+ "   *liczba losowanych punkt�w (n) dla metody Monte carlo  nie\n    powina by� liczb� ca�kowit� wi�ksz� od zera i w ramach\n    optymjalnego dzia�ania programu nie wi�ksza od 10000\n"
						+ " \n"
						+ "4.Dodatkowe opcje\n"
						+ "    *��ledzenie�� pozwala prze�ledzi� warto�ci funkcji y=f(x) w\n    danym punkcie (tylko wykres liniowy)\n"
						+ "    *ymin �zaznacza na wykresie o� y=ymin,gdzie ymin jest\n    r�wne minimalnej waro�ci f(x)\n"
						+ "    * ymax �zaznacza na wykresie o� y=ymin,gdzie ymin jest\n    r�wne minimalnej waro�ci f(x))\n"
						+ "    *opcje wykresu � po klikni�ciu prawym przyciskiem myszy\n    na obszarze wykresu dost�pne s� nast�puj�ce opcje: \n"
						+ "          - w�aciwo�ci � edytowanie w�a�ciwo�ci wyresu\n"
						+ "          - kopiowanie � kopiowanie obrazu wykresu\n"
						+ "          - pomniejszanie/powi�kszanie � dotyczy zakresu osi  OX i\n            OY\n"
						+ "          - automatyczny zakres � pozwala powr�ci� do widoku\n            automatycznego zakresu osi\n"
						+ "    *powi�kszenie � dost�pne poprzez przeci�gni�cie kursorem\n"
						+ "      myszy z naci�ni�tym lewym przyciskiem w kierunku\n      prawego dolnego rogu (r�ni si� od opcji dost�pnej\n"
						+ "      po klikni�ciu prawy przyciskiem myszy mo�liwo�ci�\n      dostosowania obszaru powi�kszenia)\n"
						+ "     *automatyczny zakres - dost�pny poprzez przeci�gni�cie\n"
						+ "      kursorem myszy z naci�ni�tym lewym przyciskiem w\n      kierunku lewego g�rnego rogu (nie r�ni si� niczym od opcji\n      dost�pnej\n"
						+ "      po klikni�ciu prawy przyciskiem myszy)\n"
						+ " \n"
						+ "5.Funkcje\n"
						+ "Wpisuj�c wz�r funkcji ca�kowanej w odpowiednim polu tekstowym nale�y stosowa� si� do zasad: \n"
						+ "   *dost�pna w u�yciu jest tylko jedna zmienna - x\n"
						+ "   *u�ytkownik dysponuje nast�puj�cymi funkcjami: Abs, Acos,\n    Asin, Atan, Atan2, Ceil, Cos, Exp, Floor, Log, Pow, Random,\n    Rint, Round, Sin, Sqrt ,Tan, ToDegrees, ToRadians\n"
						+ "   *parser obs�uguje zagnie�d�one nawiasy \n"
						+ " \n"
						+ "6.Obs�uga b��d�w\n"
						+ "W programie zawarta jest obs�uga b��d�w w przypadku podania niew�a�ciwych danych. Nast�puj�ce b��dy zosta�y uwzgl�dnione: \n"
						+ "   *niew�a�ciwy wz�r funkcji\n"
						+ "   *podana wsp�rz�dna x ko�ca przedzia�u (b) jest mniejsza\n    od wsp�rz�dnej pocz�tku (a)\n"
						+ "   *granice zakresu ca�kowania maj� niew�a�ciwy format\n"
						+ "   *funkcja nie jest ci�g�a na zadanym zakresie (obs�uga b��du\n    obejmuje wy�wietlenie podzakres�w, w obr�bie kt�rych\n    funkcja nie przyjmuje warto�ci numerycznych\n"
						+ "   *suwak ustalaj�cy warto�� dx ma zablokowan� mo�liwo��\n    ustawienia warto�ci mniejszej lub r�wnej 0\n"
						+ " \n"
						+ "7.Uwagi\n"
						+ "    Aplet zosta� napisany w j�zyku Java (JDK 1.7.0_02) z wykorzystaniem bibliotek Jfreechart (wersja 1.0.15) i Jeval (wresja 0.9.4). \n"
						+ "    Biblioteka Jfreechart zawiera b��d w opcji �ledzenia punkt�w na wykresie za pomoc� osi (pionowej i poziomej) zintegrowanych z kursorem. B��d dotyczy funkcji repaint. Gdy kursor za pierwszym razem od uruchomienia wykresu znajdzie si� na obszarze diagramu, pozostawia po sobie �lad w postaci krzy�uj�cych si� osi. B��d zanika po jednokrotnym klikni�ciu lewym przyciskiem myszy w obr�bie wykresu. \n");

		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		t.setMaximumSize(new Dimension(100,32));
		tab1.setSize(100, 100);t.setRows(20);
		JScrollPane scroll = new JScrollPane(t);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tbox.add(scroll);
		tab1.add(tbox, "Instrukcja");

		this.add(panel1);
		this.setVisible(true);
		
		/* Obliczanie obszaru i b�edu po zrenderowaniu wykresu */
		double ra=count_pixel_area(dataset1,0);
		realarea.setText("Prawdziwy Obszar:   "+ Double.toString(ra));
		error.setText("B��d:   "+ Double.toString(round(areaval-ra)));		
		y_min_max=Pomocnicze.getmax(dataset1);
		ymin.setText("y min:   "+ Double.toString(round(y_min_max[0])));
		ymax.setText("y max:   "+ Double.toString(round(y_min_max[1])));
		
		/* adnotacje (w przypadku podania z�ego przedzia�u)*/
		if(hasNaN){
			double xann=plot.getDomainAxis().getRange().getCentralValue();
			double yann=plot.getRangeAxis().getRange().getCentralValue();
			XYTextAnnotation ak = new XYTextAnnotation("Brak ci�g�o�ci funkcji na przedzia�ach \n"+ZakresNaN,xann,yann);
			ak.setFont(new Font("Tahoma", 2, 11));
			plot.addAnnotation(ak);
			hasNaN=false;
		}

		/*    Tooltips - czyli wsp�rz�ne po najechaniu mysz� na wykres */
		XYItemRenderer rendererp = plot.getRenderer();
		XYToolTipGenerator generator = new StandardXYToolTipGenerator(
		"x={1}  ,   y={2}", new DecimalFormat("0.000000"), new DecimalFormat("0.000000")
		);
		rendererp.setToolTipGenerator(generator);
		myChart.setDisplayToolTips(false);
		
	}

	class Adx implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			// TODO Auto-generated method stub
			JSlider suwak = (JSlider) e.getSource();
			suwak.getValueIsAdjusting();
			double tmp = Math.floor(dxwart) + (0.05) * suwak.getValue() ;
			tmp=round(tmp);
			if (suwak.getValueIsAdjusting() ) {
				if(tmp!=0){
					dxwart = tmp;
					dx.setText(Double.toString(dxwart));
				}
				else
					suwak.setValue(1);
				
			}
		}

	}

	class Aplus implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dxwart = (Math.round((dxwart + 1) * 100.00)) / 100.00;
			dx.setText(Double.toString(dxwart));
		}
	}

	class Aminus implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			double tmp = (Math.round((dxwart - 1) * 100.00)) / 100.00;
			if (tmp > 0) {
				dxwart = tmp;
				dx.setText(Double.toString(dxwart));
			}
		}
	}
	class Aymin implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!ymn){
				m1=new ValueMarker(y_min_max[0]);
				m1.setPaint(Color.MAGENTA);
				m1.setAlpha((float) 1.0);
				plot.addRangeMarker(m1);
				myChart.repaint();
				ymn=true;
			}else{
				plot.removeRangeMarker(m1);
				myChart.repaint();
				ymn=false;
			}
		}
	}
	class Aymax implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!ymx){
				m2=new ValueMarker(y_min_max[1]);
				m2.setPaint(Color.MAGENTA);
				m2.setAlpha((float) 1.0);
				plot.addRangeMarker(m2);
				myChart.repaint();
				ymx=true;
			}else{
				plot.removeRangeMarker(m2);
				myChart.repaint();
				ymx=false;
			}
		}
	}

	class Aok implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// setBackground(Color.black);			
			String fxtxt = fx.getText(), axtxt = ax.getText(), bxtxt = bx
					.getText();
			funkcja = fxtxt;
			try{
				a = Integer.parseInt(axtxt);
			}catch(NumberFormatException ex1){
				a=0;
				ax.setText("0");
			}
			try{
				b = Integer.parseInt(bxtxt);
			}catch(NumberFormatException ex2){
				b=1;
				bx.setText("1");
			}
			if(b<=a){
				b=a+1;
				bx.setText(Integer.toString(b));
			}
			// panel1.remove(myChart);
			JFreeChart chart = myChart.getChart();
			plot = chart.getXYPlot();			
			
			try{
				dataset1 = createDataset(a, b, funkcja, 0.001);
				y_min_max=Pomocnicze.getmax(dataset1);
				ymin.setText("y min:   "+Double.toString(round(y_min_max[0])));
				ymax.setText("y max:   "+Double.toString(round(y_min_max[1])));
				double tmp;
				
				switch (method) {
				case 0:
					plot.setRenderer(1, renderer2);
					dataset2 = MidRect.RectMidDataset(a, b, funkcja, 0.005, dxwart);
					tmp = round(MidRect.countRectMidArea(dataset2, 0, seed));
					area.setText("Obszar:   " + Double.toString(tmp));
					break;
				case 1:
					plot.setRenderer(1, renderer2);
					dataset2 = LeftRect.RectLeftDataset(a, b, funkcja, 0.005,
							dxwart);
					tmp = round(LeftRect.countRectLeftArea(dataset2, 0, seed));
					area.setText("Obszar:   " + Double.toString(tmp));
					break;
				case 2:
					plot.setRenderer(1, renderer2);
					dataset2 = RightRect.RectRightDataset(a, b, funkcja, 0.005,
							dxwart);
					tmp = round(RightRect.countRectRightArea(dataset2, 0, seed));
					area.setText("Obszar:   " + Double.toString(tmp));
					break;
				case 3:
					plot.setRenderer(1, renderer2);
					dataset2 = Trapezoid.TrapezoidDataset(a, b, funkcja, 0.005,
							dxwart);
					tmp = round(Trapezoid.countTrapezoidArea(dataset2, 0, seed));
					area.setText("Obszar:   " + Double.toString(tmp));
					break;
				case 4:
					plot.setRenderer(1, renderer2);
					dataset2 = Parabol.ParabolDataset(a, b, funkcja, 0.005, dxwart);
					tmp = round(Parabol.countParabolArea(dataset2, 0, seed));
					area.setText("Obszar:   " + Double.toString(tmp));
					break;
				case 5:
					dataset2 = MonteCarlo.createDataset(a, b, funkcja, 0.005,
							dxwart, Integer.parseInt(ena.getText()), y_min_max);
					render.setSeriesLinesVisible(0, false);
					render.setSeriesVisible(1, false);
					plot.setRenderer(1, render);
					
					tmp = round(MonteCarlo.countMonteCarloArea(a,b,dataset2, 0, seed, funkcja,y_min_max));
					area.setText("Obszar:   " + Double.toString(tmp));
					break;
				default:
					dataset2 = LeftRect.RectLeftDataset(a, b, funkcja, 0.005,dxwart);
					tmp = round(LeftRect.countRectLeftArea(dataset2, 0, seed));
					area.setText("Obszar:   " + Double.toString(tmp));
				}
				plot.setDataset(0, dataset1);
				plot.setDataset(1, dataset2);
				double ra=count_pixel_area(dataset1,0);
				realarea.setText("Prawdziwy Obszar:   "+ Double.toString(ra));
				error.setText("B��d:   "+ Double.toString(round(tmp-ra)));
			}catch (NumberFormatException exc){
				fx.setText("Niepoprawny wzr�r funkcji");
			}
			
			//-------Annotation
			
			if(hasNaN){
				plot.clearAnnotations();
				double xann=plot.getDomainAxis().getRange().getCentralValue();
				double yann=plot.getRangeAxis().getRange().getCentralValue();
				XYTextAnnotation ak = new XYTextAnnotation("Brak ci�g�o�ci funkcji na przedzia�ach \n"+ZakresNaN,xann,yann);
				ak.setFont(new Font("Tahoma", 2, 11));
				plot.addAnnotation(ak);
				hasNaN=false;
			}else
				plot.clearAnnotations();
			
			//------------------
			myChart.repaint();
			panel1.repaint();
		}
	}

	class Amid implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			n.enable(false);
			ena.enable(false);
			dxe.enable(true);
			plus.enable(true);
			minus.enable(true);
			dx.enable(true);
			method = 0;
			panel1.repaint();
		}
	}

	class Aleft implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			n.enable(false);
			ena.enable(false);
			dxe.enable(true);
			plus.enable(true);
			minus.enable(true);
			dx.enable(true);
			method = 1;
			panel1.repaint();
		}
	}

	class Aright implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			n.enable(false);
			ena.enable(false);
			dxe.enable(true);
			plus.enable(true);
			minus.enable(true);
			dx.enable(true);
			method = 2;
			panel1.repaint();
		}
	}

	class Atrap implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			n.enable(false);
			ena.enable(false);
			dxe.enable(true);
			plus.enable(true);
			minus.enable(true);
			dx.enable(true);
			method = 3;
			panel1.repaint();
		}
	}

	class Asimp implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			n.enable(false);
			ena.enable(false);
			dxe.enable(true);
			plus.enable(true);
			minus.enable(true);
			dx.enable(true);
			method = 4;
			panel1.repaint();
		}
	}

	class Amc implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			method = 5;
			n.enable(true);
			ena.enable(true);
			dxe.enable(false);
			plus.enable(false);
			minus.enable(false);
			dx.enable(false);
			panel1.repaint();
		}
	}

	private void addItem(JPanel p, JComponent c, int x, int y, int width,
			int height, int align) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = x;
		gc.gridy = y;
		gc.gridwidth = width;
		gc.gridheight = height;
		gc.weightx = 100.0;
		gc.weighty = 100.0;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = align;
		gc.fill = GridBagConstraints.NONE;
		p.add(c, gc);
	}

	private void addItem(JPanel p, JFreeChart c, int x, int y, int width,
			int height, int align) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = x;
		gc.gridy = y;
		gc.gridwidth = width;
		gc.gridheight = height;
		gc.weightx = 100.0;
		gc.weighty = 100.0;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.anchor = align;
		gc.fill = GridBagConstraints.NONE;
		// p.add(c, gc);
	}

	private  XYDataset createDataset(int xStart, int xEnd, String fx,
			double seed) {

		DefaultXYDataset dataset = new DefaultXYDataset();

		Vector<Double> vx = new Vector<Double>();
		Vector<Double> vy = new Vector<Double>();
		Vector<String> vz=new Vector<String>();
		boolean start=true;
		String zakres1="";
		String zakres2="";

		for (double i = xStart; i < xEnd; i += seed) {
			double x = Math.round(i * 1000.00) / 1000.00;
			if (i + seed > xEnd)
				x = xEnd;
			double y = Funk.fun(fx, x);			
			vx.add(x);
			vy.add(y);
			if(!Pomocnicze.isNum(y)&&x!=xEnd ){
				hasNaN=true;				
				if(start){
					zakres1="["+x+",";
					start=false;
				}else{
					zakres2=x+"]";
				}
			}else if((Pomocnicze.isNum(y)&&!start)||((x==xEnd&&!start))){
				vz.add(zakres1+zakres2);
				start=true;
			}
		}
		double[][] data = new double[2][vx.size()];
		Pomocnicze.vecToArray2d(vx, vy, data);
		dataset.addSeries("f(x)", data);
		dataset.addSeries("", data);
		
		ZakresNaN=vz.toString();
		return dataset;
	}
	private double count_pixel_area(XYDataset datas,int seria){

		ChartRenderingInfo chartInfo = new ChartRenderingInfo();
		chart.createBufferedImage(1200, 500, chartInfo);		
		PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
		
		double xpixel=plotInfo.getDataArea().getWidth();
		double ypixel=plotInfo.getDataArea().getHeight();
		double xvalue=plot.getDomainAxis().getRange().getLength();
		double yvalue=plot.getRangeAxis().getRange().getLength();
		
		double S=0;
		double prevy=0;
		double prevx=0;
		
		double XvalueOfpixel=xvalue/xpixel;
		double YvalueOfpixel=yvalue/ypixel;
		double y_zero_coord;
		
		y_zero_coord=chartInfo.getEntityCollection().getEntity(9).getArea().getBounds2D().getMinY()+(chartInfo.getEntityCollection().getEntity(9).getArea().getBounds2D().getHeight()/2);
		
		for(int i=0;i<chartInfo.getEntityCollection().getEntities().size();i++){
			
			boolean czy_XYItemEntity=chartInfo.getEntityCollection().getEntity(i).getClass().getSimpleName().equalsIgnoreCase("XYItemEntity");
			
			if(czy_XYItemEntity){
				XYItemEntity ixi=(XYItemEntity) chartInfo.getEntityCollection().getEntity(i);
				
				boolean czy_Seria=ixi.getSeriesIndex()==seria;
				boolean czy_dataset=ixi.getDataset().equals(datas);
				
				if(czy_Seria && czy_dataset){
					double x_min=ixi.getArea().getBounds2D().getMinX();
					double y_min=ixi.getArea().getBounds2D().getMinY();
					double y_max=ixi.getArea().getBounds2D().getMaxY();
					double w=ixi.getArea().getBounds2D().getWidth();
					double h=ixi.getArea().getBounds2D().getHeight();
					double y,x;
					
					if((y_min+h/2)<=y_zero_coord)						
						y=y_zero_coord-y_min;
					else{
						y=y_max-y_zero_coord;
						y*=-1;							
					}
					if(y>=0)
						y=y-2;
					else
						y=y+2;
					
					x=x_min+w/2;
					if(!(ixi.getItem()==0)){													
						S+=(y+prevy)*YvalueOfpixel*(x-prevx)*XvalueOfpixel/2;					
					}		
					prevy=y;
					prevx=x;
									
				}
				/*if(ixi.getItem()==ostatni_element){
					break;
			    }*/
			}				
		}
		S=round(S);
		return S;
	}
	
	public double round(double l){
		if(Pomocnicze.isNum(l))
			l=Math.round(l*1000000.00)/1000000.00;
		return l;
	}
	
    private JComboBox createTrace() {
        final JComboBox trace = new JComboBox();
        final String[] tArray = {"\"�ledzenie\" Wy��czone", "\"�ledzenie\" W��czone"};
        trace.setModel(new DefaultComboBoxModel(tArray));
        trace.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tArray[1].equals(trace.getSelectedItem())) {
                    myChart.setHorizontalAxisTrace(true);
                    myChart.setVerticalAxisTrace(true);
            		myChart.setDisplayToolTips(true);
                    myChart.repaint();
                } else {
                    myChart.setHorizontalAxisTrace(false);
                    myChart.setVerticalAxisTrace(false);
            		myChart.setDisplayToolTips(false);
                    myChart.repaint();
                }
            }
        });
        return trace;
    }
	
    private JComboBox createexamples() {
        final JComboBox examples = new JComboBox();
        final String[] exm = {"log(x *37+0.01) + cos(7 / (x+0.01))",
        							"acos(x)*sin(x)",
        							"1/(x+3)*exp(x+3)",
        							"abs(x)+cos(x)",
        							"cos(x)-sin(2*x)/abs(x)",
        							"tan(x)"
        							};
        examples.setModel(new DefaultComboBoxModel(exm));
        examples.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (exm[0].equals(examples.getSelectedItem())) {
                    fx.setText("log(x *37+0.01) + cos(7 / (x+0.01))");
                    ax.setText("0");
                    a=-1;
                    bx.setText("5");
                    b=0;
                    myChart.repaint();
                } else if (exm[1].equals(examples.getSelectedItem())){
                	fx.setText("acos(x)*sin(x)");
                    ax.setText("-1");
                    a=0;
                    bx.setText("1");
                    b=1;
                    myChart.repaint();
                }else if (exm[2].equals(examples.getSelectedItem())){
                	fx.setText("1/(x+3)*exp(x+3)");
                    ax.setText("0");
                    a=0;
                    bx.setText("1");
                    b=1;
                    myChart.repaint();
                }else if (exm[3].equals(examples.getSelectedItem())){
                	fx.setText("abs(x)+cos(x)");
                    ax.setText("-10");
                    a=-10;
                    bx.setText("1");
                    b=1;
                    myChart.repaint();
                }else if (exm[4].equals(examples.getSelectedItem())){
                	fx.setText("cos(x)-sin(2*x)/abs(x)");
                    ax.setText("1");
                    a=1;
                    bx.setText("5");
                    b=5;
                    myChart.repaint();
                }else if (exm[5].equals(examples.getSelectedItem())){
                	fx.setText("tan(x)");
                    ax.setText("-1");
                    a=1;
                    bx.setText("5");
                    b=5;
                    myChart.repaint();
                }
                
            }
        });
        return examples;
    }
}
