package com.example.hombr.menutester;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ReconocimientoFragment extends Fragment  {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View Rec= inflater.inflate(R.layout.reconocimiento_fragment,container,false);
        Button AddP=(Button)Rec.findViewById(R.id.AddUser);
        ImageView RaspF= (ImageView)Rec.findViewById(R.id.AddUser);
        AddP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                           }
        });

        RaspF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return Rec;
    }




}
