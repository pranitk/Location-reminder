package com.pranitkulkarni.remindbylocation.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranitkulkarni.remindbylocation.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettings extends Fragment {


    public FragmentSettings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_settings, container, false);

        rootview.findViewById(R.id.contact_developer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL,"pranitkulkarni24@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                    startActivity(intent);


            }
        });

        return rootview;
    }

}
