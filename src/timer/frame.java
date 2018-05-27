/*
 *      clase para el manejo de el frame
 */
package timer;
import java.applet.AudioClip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import javax.swing.Timer;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.File;



/**
 *
 *          @author uriel proyecto de POO 
 *          24 mayo del 2018
 *          
 * 
 */
public class frame extends JFrame{
    //Variables de clase para GUI
    final private JPanel panelIzquierdo;
    final private JPanel panelDerecho;
    private JPanel panelTareas;
    private Font fuente;
    private Font subrayado;
    private Font fuenteTitulo;
    private JLabel pomodoro;
    private JLabel workList;
    private JLabel reloj;
    private JButton play;
    private JButton pause;
    private JButton pomo;
    private JButton corto;
    private JButton largo;
    private JButton configuracion;
    private int mas = 160;
    private JButton agregarTarea;
    private JButton enviar;
    private JFrame nuevaTarea;
    private JTextField nombreDeTarea;
    private JLabel nombreT;
    private JTextArea descripcionTarea;
    private JLabel descripcionT;
    private JLabel fechaT;
    private JComboBox dia;
    private JComboBox mes;
    private JComboBox ano;
    private JMenuBar menu;
    private String dias[] = {"01", "02", "03", "04", "05", "06", "07","08", "09", "10","11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    private String meses[] = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private String anos[] = {"2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033"};
    private String minutos[] = {"1", "2", "3", "4", "5", "6", "7","8", "9", "10","11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31","32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};
    private int y = -50;
    private Timer t;
    private int limiteBL = 15;
    private int limiteBC = 5;
    private int limitePD = 25;
    private int limite = 25;
    private JFrame configuracionF;
    private JComboBox minbr;
    private JComboBox minbl;       
    private JComboBox minpm ;
    private JLabel minBC;
    private JLabel minBL;
    private JLabel minPM;
    private JButton ok;
    
    //private AudioClip sonido = new java.applet.Applet.(getClass().getResource(""));

    private int h, cs, m, s;
    
    Connection conn;
    private String driver = "org.postgresql.Driver";
    private String username = "postgres";
    private String password = "qwerty";
    
    private String insertTableSQL = "INSERT INTO tareas(nombre, descripcion, fecha_vencimiento, cumplida) VALUES(?,?,?,?)";    
    private String eliminarT = "DELETE FROM tareas WHERE nombre = ?";
    private String actualizarDatos = "UPDATE tareas SET cumplida=true WHERE nombre=?";
    
        
    private PreparedStatement insertar;
    private PreparedStatement eliminar;
    private PreparedStatement actualizar;
    private ResultSet conjuntoResultados;
    private Statement instruccion; 
    
    private ArrayList<JPanel> listaTareas = new ArrayList<JPanel>();
    
    public frame(){
        //Preferencias del frame
        super("TIMER");
        setLayout(new GridLayout(1, 2 ));
        //------------------------------
        
         t = new Timer(10, acciones);
        //Inicializaciom de variables
        menu = new JMenuBar();
        JMenu men = new JMenu("Pomodoro");
        JMenu men2 = new JMenu("WorkList");
        fuente          = new Font("sans_serif", Font.PLAIN, 20);
        fuenteTitulo    = new Font("sans_serif", Font.PLAIN, 12);
        Map  attributes = fuenteTitulo.getAttributes();
        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        subrayado = new Font(attributes); 
        panelIzquierdo  = new JPanel();
        panelDerecho    = new JPanel();
        panelTareas     = new JPanel();
        pomodoro        = new JLabel("Pomodoro");
        workList        = new JLabel("Worklist");
        reloj           = new JLabel("00:00");
        play            = new JButton("►");
        pause           = new JButton("||");
        pomo            = new JButton("Pomodoro");
        corto           = new JButton("Break Corto");
        largo           = new JButton("Break Largo");
        configuracion   = new JButton("Configuracion");
        agregarTarea    = new JButton("Agregar tarea");
        nuevaTarea      = new JFrame("Nueva tarea");
        enviar          = new JButton("Crear");
        nombreDeTarea   = new JTextField("");
        nombreT         = new JLabel("Nombre de la tarea");
        descripcionTarea = new JTextArea("");
        descripcionT    = new JLabel("Descripcion de la tarea");
        fechaT          = new JLabel("Fecha de entrega de la tarea");
        dia             = new JComboBox(dias);
        mes             = new JComboBox(meses);
        ano             = new JComboBox(anos);
        minbr           = new JComboBox(minutos);
        minbl           = new JComboBox(minutos);
        minpm           = new JComboBox(minutos);
        configuracionF = new JFrame("configuracion");
        minBC           = new JLabel("minutos para el break corto");
        minBL           = new JLabel("minutos para el break largo");
        minPM           = new JLabel("minutos para el pomodoro");
        ok              = new JButton("Ok");
        
        
        
        //-----------------------------
        configuracionF.setLayout(null);
        configuracionF.setSize(250, 300);
        configuracionF.setVisible(false);
        minBC.setBounds(25, 20, 210, 20);
        minbr.setBounds(100, 50, 50, 30);
        minBL.setBounds(25, 80, 210, 20);
        minbl.setBounds(100, 110, 50, 30);
        minPM.setBounds(25, 140, 210, 20);
        minpm.setBounds(100, 170, 50, 30);
        ok.setBounds(90, 210, 70, 35);
        ok.addActionListener(new Ok());
        configuracionF.add(minbr);
        configuracionF.add(minBC);
        configuracionF.add(minBL);
        configuracionF.add(minbl);
        configuracionF.add(minPM);
        configuracionF.add(minpm);
        configuracionF.add(ok);
        
        
        
        //Configuracion del titulo del panel izquierdo
        pomodoro.setBounds(155, 30, 250, 30);
        pomodoro.setFont(fuente);
        reloj.setBounds(90, 70, 300, 80);
        reloj.setFont(new Font("sans_serif", Font.PLAIN, 80));
        play.setBounds( 115, 160, 50, 50);
        play.addActionListener(new Start());
        pause.setBounds(245, 160, 50, 50);
        pause.addActionListener(new Stop());
        pomo.setBounds( 125, 230, 160, 30);
        pomo.addActionListener(new Pomo());
        corto.setBounds( 125, 270, 160, 30);
        corto.addActionListener(new BreakCorto());
        largo.setBounds( 125, 310, 160, 30);
        largo.addActionListener(new BreakLargo());
        configuracion.setBounds( 125, 350, 160, 30);
        configuracion.addActionListener(new Configuracion());
        //-----------------------------
        
        
        //Configuracion del panel izquierdo
        panelIzquierdo.setBorder(  BorderFactory.createEtchedBorder(1));
        panelIzquierdo.setLayout(null);
        panelIzquierdo.add(pomodoro);
        panelIzquierdo.add(reloj);
        panelIzquierdo.add(play);
        panelIzquierdo.add(pause);
        panelIzquierdo.add(pomo);
        panelIzquierdo.add(corto);
        panelIzquierdo.add(largo);
        panelIzquierdo.add(configuracion);

        //-----------------------------
        
        //configuracion del formulario de la nueva tarea
        nuevaTarea.setLayout(null);
        nuevaTarea.setSize(250, 370);
        nombreT.setBounds(50, 30, 150, 20);
        nombreDeTarea.setBounds(30, 55, 185, 50);
        descripcionT.setBounds(40, 115, 220, 20);
        //descripcionTarea.setBounds(30, 142, 185, 50);
        descripcionTarea.setLineWrap(true);
        descripcionTarea.setWrapStyleWord(true);
        Box cuadrado = Box.createHorizontalBox();
        cuadrado.add(new JScrollPane( descripcionTarea ));
        cuadrado.setBounds(30, 142, 185, 50);
        fechaT.setBounds(22, 200, 220, 20);
        dia.setBounds(12, 225, 50, 30);
        mes.setBounds(62, 225, 105, 30);
        ano.setBounds(167, 225, 70, 30);
        enviar.setBounds(65, 270, 110, 30);
        enviar.addActionListener(new EnviarDatos());
        nuevaTarea.add(mes);
        nuevaTarea.add(ano);
        nuevaTarea.add(dia);
        nuevaTarea.add(fechaT);
        nuevaTarea.add(cuadrado);
        nuevaTarea.add(descripcionT);
        nuevaTarea.add(nombreT);
        nuevaTarea.add(nombreDeTarea);
        nuevaTarea.add(enviar);
        //------------------------------
        
        //Configuracion del titulo del panel de tareas
        workList.setBounds(150, 30, 100, 30);
        workList.setFont(fuente);
        agregarTarea.setBounds(65, 70, 240, 30) ;
        agregarTarea.addActionListener(new AgregarTarea());
        //----------------------------
        
        //Configuracuib del panel de tareas
        panelTareas.setLayout(null);
        panelTareas.setPreferredSize( new Dimension(350, 380));
        panelTareas.add(workList);
        panelTareas.add(agregarTarea);
        //-----------------------------
        
        //Configuracion del panel derecho
        //panelDerecho.setBorder(  BorderFactory.createLoweredBevelBorder());
        panelDerecho.setLayout(new GridLayout(1, 1));
        panelDerecho.add(new JScrollPane(panelTareas));
        //-----------------------------
        
        //Agregar al frame
        men.setMnemonic(KeyEvent.VK_A);
        men.getAccessibleContext().setAccessibleDescription(
        "The only menu in this program that has menu items");
        JMenuItem menuItem = new JMenuItem("Ayuda",
                         KeyEvent.VK_T);
        
        men2.setMnemonic(KeyEvent.VK_A);
        men2.getAccessibleContext().setAccessibleDescription(
        "The only menu in this program that has menu items");
        JMenuItem menuItem2 = new JMenuItem("¿Que es la tecnica Pomodoro?",
                         KeyEvent.VK_T);
        menuItem2.addActionListener(new Ayuda());
        menuItem2.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new Instrucciones());
        
        men2.add(menuItem);
        men.add(menuItem2);
        menu.add(men2);
        menu.add(men);
        setJMenuBar(menu);
        
        add(panelIzquierdo);
        add(panelDerecho);
        //-----------------------------

        refresh();
    }
    
    class EnviarDatos implements ActionListener{
        @Override
        public void actionPerformed( ActionEvent evento){
            String fechaTarea = anos[ano.getSelectedIndex()] + "-" + (mes.getSelectedIndex()+1) + "-" + dias[dia.getSelectedIndex()];
            try{
                Class.forName(driver);
                conn = DriverManager.getConnection( "jdbc:postgresql://localhost:5432/postgres", username, password);
                insertar = conn.prepareStatement(insertTableSQL);
                insertar.setString(1, nombreDeTarea.getText());
                insertar.setString(2, descripcionTarea.getText());
                insertar.setDate(3, java.sql.Date.valueOf(fechaTarea));
                insertar.setBoolean(4, false);
                insertar.executeUpdate();
            }catch(ClassNotFoundException | SQLException e ){
                e.printStackTrace();
            }
            
            for(JPanel tar: listaTareas){
                panelTareas.remove(tar);
            }
            refresh();
            //cierra el formulario y reinicia valores
            nombreDeTarea.setText("");
            descripcionTarea.setText("");
            nuevaTarea.setVisible(false);
        }
    }
    
    //clase que despliega el formulario de la tarea
    class AgregarTarea implements ActionListener{    
        @Override
        public void actionPerformed( ActionEvent evento){
            nuevaTarea.setVisible(true);
        }
    }
    
    class TareaCumplida implements ActionListener{
        @Override 
        public void actionPerformed( ActionEvent evento){
            try{
                Class.forName(driver);
                conn = DriverManager.getConnection( "jdbc:postgresql://localhost:5432/postgres", username, password);
                actualizar = conn.prepareStatement(actualizarDatos);
                actualizar.setString(1, evento.getActionCommand());
                actualizar.executeUpdate();
                
            }catch(ClassNotFoundException | SQLException e ){
                e.printStackTrace();
            }
             for(JPanel tar: listaTareas){
                 panelTareas.remove(tar);
             }
            refresh();
        }
    }
    
    class EliminarTarea implements ActionListener{
        @Override
        public void actionPerformed( ActionEvent evento){
             try{
                Class.forName(driver);
                conn = DriverManager.getConnection( "jdbc:postgresql://localhost:5432/postgres", username, password);
                eliminar = conn.prepareStatement(eliminarT);
                eliminar.setString(1, evento.getActionCommand());
                eliminar.execute();
            }catch(ClassNotFoundException | SQLException e ){
                e.printStackTrace();
            }
             for(JPanel tar: listaTareas){
                panelTareas.remove(tar);
            }
            refresh();
        }
    }
    
    private void refresh(){
        try {
            int y = -50;
            int mas = 160;

            Class.forName(driver);
            conn = DriverManager.getConnection( "jdbc:postgresql://localhost:5432/postgres", username, password);
            instruccion = conn.createStatement();
            conjuntoResultados = instruccion.executeQuery("select * from tareas");

            ResultSetMetaData meta = conjuntoResultados.getMetaData();
            int numeroDeColumnas = meta.getColumnCount();

            while( conjuntoResultados.next()){
                mas += 170;
                
                y += 170;
                JPanel tarea = new JPanel();

                tarea.setLayout(null);
                tarea.setBorder(  BorderFactory.createEtchedBorder(1));

                JLabel titulo = new JLabel(String.format("%s", conjuntoResultados.getObject(1)));
                String funteE = String.format("%s", conjuntoResultados.getObject(4));
                titulo.setFont(Boolean.parseBoolean(funteE) ? subrayado : fuenteTitulo);
                JCheckBox borrar = new JCheckBox(String.format("%s", conjuntoResultados.getObject(1)));
                JCheckBox terminada = new JCheckBox(String.format("%s", conjuntoResultados.getObject(1)));
                JLabel tareaTerminada = new JLabel("Terminada ? ");
                tareaTerminada.setBounds(180, 128, 100, 20);
                terminada.setBounds(275, 128, 20, 20 );
                terminada.addActionListener(new TareaCumplida());
                borrar.setBounds( 285, 5 , 20, 20);
                borrar.addActionListener(new EliminarTarea());
                titulo.setBounds(20, 10, 150, 30);
                tarea.add(tareaTerminada);
                tarea.add(terminada);
                tarea.add(borrar);
                tarea.add(titulo);  

                Box cuadrado1 = Box.createHorizontalBox();
                JTextArea descripcion = new JTextArea(String.format("%s", conjuntoResultados.getObject(2)));
                descripcion.setLineWrap(true);
                descripcion.setWrapStyleWord(true);
                descripcion.setEditable(false);
                cuadrado1.add(new JScrollPane( descripcion ));

                cuadrado1.setBounds(20, 40, 275, 80);
                tarea.add(cuadrado1);

                JLabel vencimiento = new JLabel(String.format("%s", conjuntoResultados.getObject(3)));
                vencimiento.setBounds(180, 10, 150, 30);
                tarea.add(vencimiento);
                
                tarea.setBounds(40, y, 310, 160);
                listaTareas.add(tarea);
                panelTareas.add(tarea);

                }
                panelTareas.setPreferredSize(new Dimension(350, mas));
        } catch (ClassNotFoundException | SQLException e ){
            e.printStackTrace();
        }
        panelTareas.updateUI();
        
    }
    private ActionListener acciones = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent evento){
            ++cs; 
            if(cs == 100){
                cs = 0;
                ++s;
            }
            if(s == 60){
                s = 0;
                ++m;
            }
            if( m == 60){
                m = 0;
                ++h;
            }
            actualizarLabel();
            if(m == limite){
           
                t.stop();
                sonido();
            }
        }
        
    };
    private void actualizarLabel() {
        String tiempo = (m<=9?"0":"")+m+":"+(s<=9?"0":"")+s;
        reloj.setText(tiempo);
    }
    
    class Start implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evento){
            t.start();
        }
    }
    
    class Stop implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evento){
            t.stop();
        }
    }
    
    class BreakCorto implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evento){
            reiniciar();
            pomodoro.setText("Break corto");
            pomodoro.setBounds(150, 30, 250, 30);
            limite = limiteBC;
        }
    }
    class BreakLargo implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evento){
            pomodoro.setText("Break largo");
            pomodoro.setBounds(150, 30, 250, 30);
            reiniciar();
            limite = limiteBL;
        }
    }
    
    class Pomo implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evento){
            pomodoro.setText("Pomodoro");
            pomodoro.setBounds(155, 30, 250, 30);
            reiniciar();
            limite = limitePD;
        }
    }
    
    class Configuracion implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evento){
            configuracionF.setVisible(true);
        }
    }
    public void reiniciar(){
        String tiempo = "00:00";
        reloj.setText(tiempo);
        h = 0;
        m = 0;
        s = 0;
        cs = 0;
        t.stop();
    }
    
    class Ok implements ActionListener{
    @Override
        public void actionPerformed(ActionEvent evento){
            configuracionF.setVisible(false);
            limiteBC = minbr.getSelectedIndex() + 1;
            limiteBL = minbl.getSelectedIndex() + 1;
            limitePD = minpm.getSelectedIndex() + 1;
        }
    }
    
    public void sonido(){
        AudioClip sonido;
        sonido = java.applet.Applet.newAudioClip(getClass().getResource("/timer/campana.wav"));
        sonido.play();
    }
    
    class Ayuda implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evento){
            JOptionPane.showMessageDialog(null, "La tecnica pomodoro");
        }
    }
    
    class Instrucciones implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evento){
            JOptionPane.showMessageDialog(null, "Agrega una tarea");
        }
    }

}
