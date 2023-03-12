package com.example.primerproyecto.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.primerproyecto.Clases.Pago;
import com.example.primerproyecto.R;

import java.util.ArrayList;

public class PagoAdapter extends BaseAdapter {

    private ArrayList<Pago> pagos;
    private String divisa;
    private LayoutInflater inflater;

    public PagoAdapter(Context context, ArrayList<Pago> pPagos, String pDivisa) {
        pagos = pPagos;
        divisa = pDivisa;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return pagos.size();
    }

    @Override
    public Object getItem(int i) {
        return pagos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position,@NonNull View view,@NonNull ViewGroup viewGroup){

        view = inflater.inflate(R.layout.pago_item, null);

        TextView tAutorPago = view.findViewById(R.id.tAutorPago);
        TextView tDestPago = view.findViewById(R.id.tDestPago);
        TextView tCantidadPago = view.findViewById(R.id.tCantidadPago);
        ImageView iGrupo =(ImageView) view.findViewById(R.id.iGrupo);

        Pago gasto = pagos.get(position);

        tAutorPago.setText(gasto.getAutor().getNombre());
        tDestPago.setText(gasto.getDestinatario().getNombre());
        tCantidadPago.setText(gasto.getCantidad() + " " + divisa);
        iGrupo.setImageResource(R.drawable.flecha_naranja);

        return view;
    }
}
