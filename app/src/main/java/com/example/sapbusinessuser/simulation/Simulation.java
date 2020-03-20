package com.example.sapbusinessuser.simulation;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sapbusinessuser.R;

public class Simulation extends AppCompatActivity {
    Button simul1, simul2, simul3, simul4, simul5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
       /* simul1 = findViewById(R.id.simulation1);
        simul1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Simulation.this, SimulationOne.class));
            }
        });
        simul2 = findViewById(R.id.simulation2);
        simul2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Simulation.this, SimulationTwo.class));
            }
        });
        simul3 = findViewById(R.id.simulation3);
        simul3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Simulation.this, SimulationThree.class));
            }
        });
        simul4 = findViewById(R.id.simulation4);
        simul4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Simulation.this, SimulationFour.class));
            }
        });*/


    }

}
