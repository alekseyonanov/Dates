package com.nollpointer.dates.views;

import androidx.cardview.widget.CardView;
import android.widget.EditText;
import android.widget.ImageView;

public class DateSearchView{

    CardView searchContainer;
    EditText searchEditText;
    ImageView backButton;
    ImageView clearButton;

    public void hide(){

    }

    public void show(){

    }

}


//public class DateSearchView extends RelativeLayout {
//
//    List<Date> dates;
//    RecyclerView recyclerView;
//    TextView textView;
//
//    public DateSearchView(Context context) {
//        super(context);
//        setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//        recyclerView = new RecyclerView(context);
//        textView = new TextView(context);
//        addView(recyclerView,new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//        addView(textView,new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//        textView.setGravity(Gravity.CENTER);
//        textView.setVisibility(GONE);
//    }
//
//    public void setDates(List<Date> dates){
//        this.dates = dates;
//
//        SearchCardsAdapter adapter = new SearchCardsAdapter(dates);
//        adapter.setListener(new SearchCardsAdapter.Listener() {
//            @Override
//            public void onItemClick(Date clickedDate) {
//
//            }
//        });
//
//        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
//                linearLayout.getOrientation());
//
//        recyclerView.setLayoutManager(linearLayout);
//        recyclerView.addItemDecoration(dividerItemDecoration);
//        recyclerView.setAdapter(adapter);
//
//    }
//
//
//
//    public void search(String query) {
//        ArrayList<Date> querySearch = new ArrayList<>();
//        for (Date date : dates) {
//            if(date.contains(query))
//                querySearch.add(date);
//        }
//        SearchCardsAdapter adapter = ((SearchCardsAdapter) recyclerView.getAdapter());
//        adapter.refreshList(querySearch);
//    }
//}
