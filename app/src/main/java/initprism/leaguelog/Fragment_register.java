package initprism.leaguelog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import misc.OnSingleClickListener;

public class Fragment_register extends Fragment{


    public Fragment_register(){
    }

    LinearLayout register;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        context = view.getContext();

        final String platform = getArguments().getString("platform");

        register = (LinearLayout) view.findViewById(R.id.mRegister);
        register.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.putExtra("platform", platform);
                startActivity(intent);
            }
        });


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}
