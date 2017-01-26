package com.danny.apps;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.danny.apps.materialtests.R;
import com.swapi.models.People;
import com.swapi.models.SWModelList;
import com.swapi.sw.StarWarsApi;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit.Callback;
import retrofit.RetrofitError;


/**
 * A simple {@link Fragment} subclass.
 */
public class FemaleFragment extends Fragment {

    ListView listView;
    ArrayList<People> peoples;
    int page = 2;
    Button loadMore;


    public FemaleFragment() {
        // Required empty public constructor
        peoples = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_female, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.list_female);
        loadMore = (Button) view.findViewById(R.id.load_more);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore();
            }
        });
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//
//            }
//
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//
//                if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount!=0)
//                {
//                    loadMore();
//                }
//            }
//        });
        if (peoples.isEmpty()){
            getStarWarsPeoples();
        } else {
            setPeoples(peoples);
        }
    }

    public void setPeoples (ArrayList<People> arrayList) {
        if (getActivity() != null){
            PeopleAdapter adapter = new PeopleAdapter(getActivity(), arrayList);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void getFilteredPeoples(String home) {
        ArrayList<People> filteredPeoples = new ArrayList<>();
        for (People p : peoples) {
            if (p.home.equalsIgnoreCase(home)) {
                filteredPeoples.add(p);
            }
        }

        setPeoples(filteredPeoples);

    }

    public void getStarWarsPeoples() {
        if(getActivity() == null) return;
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
        StarWarsApi.getApi().getAllPeople(1, new Callback<SWModelList<People>>() {
            @Override
            public void success(SWModelList<People> peopleSWModelList, retrofit.client.Response response) {
                peoples.clear();
                for (People p : peopleSWModelList.results) {

                    if (!p.gender.equalsIgnoreCase("male")) {
                        peoples.add(p);
                    }

                }
                setPeoples(peoples);
                if (peoples.isEmpty()) loadMore();
                if (dialog.isShowing()) dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if (dialog.isShowing()) dialog.dismiss();
            }
        });
    }

    public void loadMore() {
        page++;
        if (getActivity() == null) return;
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialog.show();

        StarWarsApi.getApi().getAllPeople(page, new Callback<SWModelList<People>>() {
            @Override
            public void success(SWModelList<People> peopleSWModelList, retrofit.client.Response response) {
                for (People p : peopleSWModelList.results) {

                    if (!p.gender.equalsIgnoreCase("male")) {
                        peoples.add(p);
                    }

                }
                setPeoples(peoples);
                if (peoples.isEmpty()) loadMore();
                if (dialog.isShowing()) dialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                if (dialog.isShowing()) dialog.dismiss();
            }
        });
    }
}
