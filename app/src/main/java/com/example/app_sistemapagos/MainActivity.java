package com.example.app_sistemapagos;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<ArrayList<String>> cargos = new ArrayList<>();
    ArrayList<ArrayList<String>> personal = new ArrayList<>();
    ArrayList<ArrayList<String>> planillas = new ArrayList<>();

    ArrayList<ArrayList<String>> bonos = new ArrayList<>();
    ArrayList<ArrayList<String>> descuentos = new ArrayList<>();
    TextView txtNombre, txtBasico, txtBono, txtDescuento, txtLiq;
    EditText edtCarnet;
    Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initComponentes();
        verificarPermisos();
    }


    public void buscar(View v){
        int carnet = Integer.parseInt(edtCarnet.getText().toString());
        String nombre = "", cargo = "", bono = "", descuento = "";
        double basico = 0, liquido = 0;
        double bonoo =0, descuentoo =0;
        for(int i = 1; i < personal.size(); i++){
            if(Integer.parseInt(personal.get(i).get(0)) == carnet){
                nombre = personal.get(i).get(3) + " " + personal.get(i).get(2) + " " + personal.get(i).get(1);

                break;
            }
        }

        System.out.println("nombre: " + nombre);

        // buscar cargo
        String idCargo = "";
        for(int i = 1; i < planillas.size(); i++){
            if(Integer.parseInt(planillas.get(i).get(1)) == carnet){
                idCargo = planillas.get(i).get(2);
                break;
            }
        }
        System.out.println("idCargo: " + idCargo);

        for(int i = 1; i < cargos.size(); i++){
            if(cargos.get(i).get(0).equals(idCargo)){
                //cargo = cargos.get(i).get(1);
                basico = Double.parseDouble(cargos.get(i).get(2));
                break;
            }
        }

        // buscar bonos
        for(int i = 1; i < bonos.size(); i++){
            if(Integer.parseInt(bonos.get(i).get(1)) == carnet){
                bonoo += Double.parseDouble(bonos.get(i).get(2));

            }
        }
        //4928737
        System.out.println("bono: " + bono);

        // buscar descuentos
        for(int i = 1; i < descuentos.size(); i++){
            if(Integer.parseInt(descuentos.get(i).get(1)) == carnet){
                descuentoo += Double.parseDouble(descuentos.get(i).get(2));
            }
        }

        if(bono.equals("")){
            bono = "0";
        }
        if(descuento.equals("")){
            descuento = "0";
        }

        liquido = basico + bonoo - descuentoo;

        txtNombre.setText(nombre);
        txtBasico.setText(String.valueOf(basico));
        txtBono.setText(String.valueOf(bonoo));
        txtDescuento.setText(String.valueOf(descuentoo));
        txtLiq.setText(String.valueOf(liquido));


    }

    //4844828

    //ejer2 53889, 88 personal, planilla 66, 22 que no cobran eso mostrar
    public void initComponentes(){
        txtNombre = findViewById(R.id.txtNombre);
        txtBasico = findViewById(R.id.txtBasico);
        txtBono = findViewById(R.id.txtBono);
        txtDescuento = findViewById(R.id.txtDescuento);
        txtLiq = findViewById(R.id.txtLiq);
        edtCarnet = findViewById(R.id.edtCarnet);
        btnBuscar = findViewById(R.id.btnBuscar);
    }


    public void verificarPermisos(){
        accessFile();
    }
    // Método para acceder al archivo después de que se conceda el permiso
    private void accessFile() {
        String estado = Environment.getExternalStorageState();
        if (!estado.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "No se encuentra la tarjeta SD", Toast.LENGTH_SHORT).show();
            return;
        }



        File dir = Environment.getExternalStorageDirectory();
        File p = new File(dir.getAbsolutePath() + File.separator + "desctos.csv");
        File p1 = new File(dir.getAbsolutePath() + File.separator + "bonos.csv");
        File p2 = new File(dir.getAbsolutePath() + File.separator + "planillas.csv");
        File p3 = new File(dir.getAbsolutePath() + File.separator + "personal.csv");
        File p4 = new File(dir.getAbsolutePath() + File.separator + "cargos.csv");
        try {
            BufferedReader lector = new BufferedReader(new FileReader(p));
            BufferedReader lector1 = new BufferedReader(new FileReader(p1));
            BufferedReader lector2 = new BufferedReader(new FileReader(p2));
            BufferedReader lector3 = new BufferedReader(new FileReader(p3));
            BufferedReader lector4 = new BufferedReader(new FileReader(p4));
            StringBuilder res = new StringBuilder();
            String linea;

            while ((linea = lector.readLine()) != null) {
                descuentos.add(new ArrayList<>(List.of(linea.split(";"))));

            }

            while((linea = lector1.readLine()) != null){
                bonos.add(new ArrayList<>(List.of(linea.split(";"))));
            }

            while((linea = lector2.readLine()) != null){
                planillas.add(new ArrayList<>(List.of(linea.split(";"))));
            }

            while((linea = lector3.readLine()) != null){
                personal.add(new ArrayList<>(List.of(linea.split(";"))));
            }

            while((linea = lector4.readLine()) != null){
                cargos.add(new ArrayList<>(List.of(linea.split(";"))));
            }


            lector.close();
        } catch (IOException e) {
            Toast.makeText(this,  e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}