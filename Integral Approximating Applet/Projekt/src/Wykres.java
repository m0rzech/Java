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

	JLabel area = new JLabel("Obszar:"), n = new JLabel("(liczba punktów) n:"),
			realarea = new JLabel("Prawdziwy Obszar:"),error = new JLabel("B³¹d:"),ymin = new JLabel(),ymax = new JLabel();
	

	double[] y_min_max;

	JButton ok = new JButton("rysuj wykres"), plus = new JButton(" + 1"),
			minus = new JButton(" - 1"),yminbutton = new JButton("y min"),ymaxbutton = new JButton("y max");

	JRadioButton Middle = new JRadioButton("ŒrodkowegoPunktu"), Left = new JRadioButton(
			"LewegoPunktu"), Right = new JRadioButton("PrawegoPunktu"),
			Trapez = new JRadioButton("Trapezów"), Simpson = new JRadioButton(
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

		// w³aœciwoœci panelu
		panel1.setLayout(new GridBagLayout());

		
		Box b1 = Box.createVerticalBox();
		b1.add(new JLabel("Funkcja:"));
		b1.add(new JLabel("(pocz¹tek przedzia³u) a:"));
		b1.add(new JLabel("(koniec przedzia³u) b:"));
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
				"Aproksymacja ca³ki oznaczonej za pomoc¹ metod numerycznych",
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
		exampl.setBorder(BorderFactory.createTitledBorder("Przyk³ady"));

		/* listenery do radiobuttonów */

		Middle.addActionListener(new Amid());
		Left.addActionListener(new Aleft());
		Right.addActionListener(new Aright());
		Trapez.addActionListener(new Atrap());
		Simpson.addActionListener(new Asimp());
		Monte.addActionListener(new Amc());

		// ustalamy pierwszy algorytm za wybrany
		Middle.setSelected(true);

		/*
		 * box2 w którym znajduj¹ sie przyciski i poletextowe odpowiadaj¹ce za
		 * parametr dx
		 */
		Box box2 = Box.createHorizontalBox();
		box2.add(minus);
		box2.add(dx);
		box2.add(plus);
		/* box3 box w którym znajduje sie box1 i slider ustalaj¹cy paraetr dx */
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
						+ "  Aplet s³u¿y ilustrowania oszacowañ wartoœci ca³ki oznaczonej za pomoc¹ wybranych metod."
						+ " Pozwala przeœledziæ ró¿nice w obliczeniach dla ró¿nych algorytmów. "
						+ " Istnieje mo¿liwoœæ podania w³asnych danych, b¹dŸ wczytania gotowych."
						+ " przyk³¹dów. \n"
						+ " \n"
						+ "2.Dane uwzglêdniane w obliczeniach obejmuj¹: \n"
						+ "    *a – pocz¹tek przedzia³u\n"
						+ "    *b – koniec przedzia³u ca³kowania\n"
						+ "    *n – liczba losowanych punktów (MonteCarlo)\n"
						+ "    *dx – d³ugoœæ podprzedzia³ów ca³kowania; \n"
						+ "     d³ugoœæ podstawy ka¿dego sumowanego wielok¹ta(Sumy\n    Riemanna) \n"
						+ "     *algorytm wykorzystywany w obliczeniach obszaru pod\n    wykresem\n"
						+ " \n"
						+ "3.Zasady korzystania\n"
						+ "Aby unikn¹æ podania b³êdnych danych ale¿y stosowaæ siê do zasad: \n"
						+ "   *wartoœæ pocz¹tku przedzia³u (a) powinna byæ mniejsza od\n    wartoœci koñca przedzia³u (b)\n"
						+ "   *funkcja powinna byæ ca³kowalna na zadanym zakresie\n"
						+ "   *wspó³rzêdne granic zakresu powinny byæ podane w\n    formacie liczb ca³kowitych\n"
						+ "   *aby program dzia³a³ optymalnie zakres nie powinien\n    przekraczaæ 100\n"
						+ "   *liczba losowanych punktów (n) dla metody Monte carlo  nie\n    powina byæ liczb¹ ca³kowit¹ wiêksz¹ od zera i w ramach\n    optymjalnego dzia³ania programu nie wiêksza od 10000\n"
						+ " \n"
						+ "4.Dodatkowe opcje\n"
						+ "    *„Œledzenie”– pozwala przeœledziæ wartoœci funkcji y=f(x) w\n    danym punkcie (tylko wykres liniowy)\n"
						+ "    *ymin –zaznacza na wykresie oœ y=ymin,gdzie ymin jest\n    równe minimalnej waroœci f(x)\n"
						+ "    * ymax –zaznacza na wykresie oœ y=ymin,gdzie ymin jest\n    równe minimalnej waroœci f(x))\n"
						+ "    *opcje wykresu – po klikniêciu prawym przyciskiem myszy\n    na obszarze wykresu dostêpne s¹ nastêpuj¹ce opcje: \n"
						+ "          - w³aciwoœci – edytowanie w³aœciwoœci wyresu\n"
						+ "          - kopiowanie – kopiowanie obrazu wykresu\n"
						+ "          - pomniejszanie/powiêkszanie – dotyczy zakresu osi  OX i\n            OY\n"
						+ "          - automatyczny zakres – pozwala powróciæ do widoku\n            automatycznego zakresu osi\n"
						+ "    *powiêkszenie – dostêpne poprzez przeci¹gniêcie kursorem\n"
						+ "      myszy z naciœniêtym lewym przyciskiem w kierunku\n      prawego dolnego rogu (ró¿ni siê od opcji dostêpnej\n"
						+ "      po klikniêciu prawy przyciskiem myszy mo¿liwoœci¹\n      dostosowania obszaru powiêkszenia)\n"
						+ "     *automatyczny zakres - dostêpny poprzez przeci¹gniêcie\n"
						+ "      kursorem myszy z naciœniêtym lewym przyciskiem w\n      kierunku lewego górnego rogu (nie ró¿ni siê niczym od opcji\n      dostêpnej\n"
						+ "      po klikniêciu prawy przyciskiem myszy)\n"
						+ " \n"
						+ "5.Funkcje\n"
						+ "Wpisuj¹c wzór funkcji ca³kowanej w odpowiednim polu tekstowym nale¿y stosowaæ siê do zasad: \n"
						+ "   *dostêpna w u¿yciu jest tylko jedna zmienna - x\n"
						+ "   *u¿ytkownik dysponuje nastêpuj¹cymi funkcjami: Abs, Acos,\n    Asin, Atan, Atan2, Ceil, Cos, Exp, Floor, Log, Pow, Random,\n    Rint, Round, Sin, Sqrt ,Tan, ToDegrees, ToRadians\n"
						+ "   *parser obs³uguje zagnie¿d¿one nawiasy \n"
						+ " \n"
						+ "6.Obs³uga b³êdów\n"
						+ "W programie zawarta jest obs³uga b³êdów w przypadku podania niew³aœciwych danych. Nastêpuj¹ce b³êdy zosta³y uwzglêdnione: \n"
						+ "   *niew³aœciwy wzór funkcji\n"
						+ "   *podana wspó³rzêdna x koñca przedzia³u (b) jest mniejsza\n    od wspó³rzêdnej pocz¹tku (a)\n"
						+ "   *granice zakresu ca³kowania maj¹ niew³aœciwy format\n"
						+ "   *funkcja nie jest ci¹g³a na zadanym zakresie (obs³uga b³êdu\n    obejmuje wyœwietlenie podzakresów, w obrêbie których\n    funkcja nie przyjmuje wartoœci numerycznych\n"
						+ "   *suwak ustalaj¹cy wartoœæ dx ma zablokowan¹ mo¿liwoœæ\n    ustawienia wartoœci mniejszej lub równej 0\n"
						+ " \n"
						+ "7.Uwagi\n"
						+ "    Aplet zosta³ napisany w jêzyku Java (JDK 1.7.0_02) z wykorzystaniem bibliotek Jfreechart (wersja 1.0.15) i Jeval (wresja 0.9.4). \n"
						+ "    Biblioteka Jfreechart zawiera b³¹d w opcji œledzenia punktów na wykresie za pomoc¹ osi (pionowej i poziomej) zintegrowanych z kursorem. B³¹d dotyczy funkcji repaint. Gdy kursor za pierwszym razem od uruchomienia wykresu znajdzie siê na obszarze diagramu, pozostawia po sobie œlad w postaci krzy¿uj¹cych siê osi. B³¹d zanika po jednokrotnym klikniêciu lewym przyciskiem myszy w obrêbie wykresu. \n");

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
		
		/* Obliczanie obszaru i b³edu po zrenderowaniu wykresu */
		double ra=count_pixel_area(dataset1,0);
		realarea.setText("Prawdziwy Obszar:   "+ Double.toString(ra));
		error.setText("B³¹d:   "+ Double.toString(round(areaval-ra)));		
		y_min_max=Pomocnicze.getmax(dataset1);
		ymin.setText("y min:   "+ Double.toString(round(y_min_max[0])));
		ymax.setText("y max:   "+ Double.toString(round(y_min_max[1])));
		
		/* adnotacje (w przypadku podania z³ego przedzia³u)*/
		if(hasNaN){
			double xann=plot.getDomainAxis().getRange().getCentralValue();
			double yann=plot.getRangeAxis().getRange().getCentralValue();
			XYTextAnnotation ak = new XYTextAnnotation("Brak ci¹g³oœci funkcji na przedzia³ach \n"+ZakresNaN,xann,yann);
			ak.setFont(new Font("Tahoma", 2, 11));
			plot.addAnnotation(ak);
			hasNaN=false;
		}

		/*    Tooltips - czyli wspó³rzêne po najechaniu mysz¹ na wykres */
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
				error.setText("B³¹d:   "+ Double.toString(round(tmp-ra)));
			}catch (NumberFormatException exc){
				fx.setText("Niepoprawny wzrór funkcji");
			}
			
			//-------Annotation
			
			if(hasNaN){
				plot.clearAnnotations();
				double xann=plot.getDomainAxis().getRange().getCentralValue();
				double yann=plot.getRangeAxis().getRange().getCentralValue();
				XYTextAnnotation ak = new XYTextAnnotation("Brak ci¹g³oœci funkcji na przedzia³ach \n"+ZakresNaN,xann,yann);
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
        final String[] tArray = {"\"Œledzenie\" Wy³¹czone", "\"Œledzenie\" W³¹czone"};
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
