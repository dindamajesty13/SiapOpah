package com.majesty.siapopa.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.majesty.siapopa.Common;
import com.majesty.siapopa.R;
import com.majesty.siapopa.adapter.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView status, username, nama, wilayah_kerja;
    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    ViewPager viewPager;
    View mIndicator;
    private int indicatorWidth;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tablayout);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        viewPager = view.findViewById(R.id.viewpager);
        mIndicator = view.findViewById(R.id.indicator);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new Harian(), "Harian");
        adapter.AddFragment(new SetengahBulan(), "Setengah Bulan");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        status = view.findViewById(R.id.status);
        username = view.findViewById(R.id.username);
        nama = view.findViewById(R.id.nama);
        wilayah_kerja = view.findViewById(R.id.wilayah_kerja);


        if (Common.currentUser.getId_usergroup().equals("1")) {
            status.setText("Profesi: POPT");
        } else if (Common.currentUser.getId_usergroup().equals("2")) {
            status.setText("Profesi: Kortikab");
        } else if (Common.currentUser.getId_usergroup().equals("3")) {
            status.setText("Profesi: SatPel");
        } else if (Common.currentUser.getId_usergroup().equals("4")) {
            status.setText("Profesi: BPTPH");
        }
        username.setText("Username: " + Common.currentUser.getUsername());
        nama.setText("Nama: " + Common.currentUser.getNama());
        wilayah_kerja.setText("Wilayah Kerja: " + Common.currentUser.getAlamat());

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = tabLayout.getWidth() / tabLayout.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return view;
    }


}