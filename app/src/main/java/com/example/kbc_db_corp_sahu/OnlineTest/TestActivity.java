package com.example.kbc_db_corp_sahu.OnlineTest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kbc_db_corp_sahu.R;
import com.example.kbc_db_corp_sahu.Sharepref.SharePref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestActivity extends Activity {
    ArrayList<QuestionPojo> pages;
    private ViewPager viewpager;
    DatabaseReference dbref;
    LinearLayout animLayout;
    TextView totque;
    int cnt, counterValue = 30000;

    CustomPagerAdapter customPagerAdapter;
    CountDownTimer mCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        setContentView(R.layout.activity_test);
        SharePref.init(this);
        SharePref.clearAll();
        dbref = FirebaseDatabase.getInstance().getReference();

        viewpager = (ViewPager) findViewById(R.id.viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (customPagerAdapter != null) {
                    animLayout.setVisibility(View.INVISIBLE);
                    cnt = position + 1;
                    customPagerAdapter.setAnim(animLayout);
                    customPagerAdapter.setData(cnt);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.beginFakeDrag();
        getDataViaVolley();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
        SharePref.clearAll();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharePref.init(this);

        SharePref.clearAll();

    }

    void getDataViaVolley() {
        dbref.child("KBC").child("QL").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();

                pages = new ArrayList();
                for (DataSnapshot dataSnapshot1 : dataSnapshotIterable) {
                    QuestionPojo lang = dataSnapshot1.getValue(QuestionPojo.class);
                    pages.add(lang);


                }


                customPagerAdapter = new CustomPagerAdapter(TestActivity.this, pages, viewpager);
                viewpager.setAdapter(customPagerAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<QuestionPojo> list;
        ViewPager pager;
        int count = 0, cnt = 1;
        Handler handler = null;
        Animation animation = null;


        public CustomPagerAdapter(Context context, ArrayList<QuestionPojo> pages, ViewPager viewpager) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            list = pages;
            pager = viewpager;
            handler = new Handler();


        }

        // Returns the number of pages to be displayed in the ViewPager.
        @Override
        public int getCount() {
            return list.size();
        }

        // Returns true if a particular object (page) is from a particular page
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // This method should create the page for the given position passed to it as an argument.
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // Inflate the layout for the page
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);


            final TextView opt1 = itemView.findViewById(R.id.optionA);
            final TextView opt2 = itemView.findViewById(R.id.optionB);
            final TextView opt3 = itemView.findViewById(R.id.optionC);
            final TextView opt4 = itemView.findViewById(R.id.optionD);
            totque = itemView.findViewById(R.id.totque);

            final LinearLayout ALinearLayout = itemView.findViewById(R.id.ALinearLayout);
            final LinearLayout BLinearLayout = itemView.findViewById(R.id.BLinearLayout);
            final LinearLayout CLinearLayout = itemView.findViewById(R.id.CLinearLayout);
            final LinearLayout DLinearLayout = itemView.findViewById(R.id.DLinearLayout);
            final TextView skip = itemView.findViewById(R.id.skip);
            final TextView counter = itemView.findViewById(R.id.counter);
            animLayout = itemView.findViewById(R.id.anim_layout);
            final TextView ans = itemView.findViewById(R.id.answer);
            final TextView que = itemView.findViewById(R.id.question);
            animLayout.setVisibility(View.INVISIBLE);
//           mCounter= new CountDownTimer(counterValue, 1000) {
//
//                public void onTick(long millisUntilFinished) {
//                    counter.setText("seconds remaining: " + millisUntilFinished / 1000);
//                    //here you can have your logic to set text to edittext
//                }
//
//                public void onFinish() {
//                    initSP();
//
//                    SharePref.write(SharePref.MARKS, (count - 5));
//                    checkCorrectAns(linearLayout, ans, ALinearLayout, BLinearLayout, CLinearLayout, DLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorGreen));
//
//                }
//
//            }.start();

            setAnim(animLayout);
            setData(cnt);
            que.setText(list.get(position).getTestQues());
            opt1.setText("A. " + list.get(position).getOption1());
            opt2.setText("B. " + list.get(position).getOption2());
            opt3.setText("C. " + list.get(position).getOption3());
            opt4.setText("D. " + list.get(position).getOption4());

            pager.beginFakeDrag();
            container.addView(itemView);
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initSP();
                    SharePref.write(SharePref.MARKS, (count - 5));
                    checkCorrectAns(animLayout, ans, ALinearLayout, BLinearLayout, CLinearLayout, DLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorGreen));
                }
            });
            ALinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getOption1().equals(list.get(position).getAnswer())) {
                        initSP();
                        SharePref.write(SharePref.MARKS, (count + 20));
                        ALinearLayout.setBackgroundResource(R.drawable.halfcirclegreen);
                        checkCorrectAns(animLayout, ans, ALinearLayout, BLinearLayout, CLinearLayout, DLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorGreen));

                    } else {
                        initSP();
                        SharePref.write(SharePref.MARKS, count - 10);
                        ALinearLayout.setBackgroundResource(R.drawable.halfcornerred);
                        checkCorrectAns(animLayout, ans, ALinearLayout, BLinearLayout, CLinearLayout, DLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorRed));

                    }
                }
            });

            BLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getOption2().equals(list.get(position).getAnswer())) {
                        initSP();
                        SharePref.write(SharePref.MARKS, (count + 20));
                        checkCorrectAns(animLayout, ans, BLinearLayout, ALinearLayout, CLinearLayout, DLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorGreen));
                        BLinearLayout.setBackgroundResource(R.drawable.halfcirclegreen);

                    } else {
                        initSP();
                        SharePref.write(SharePref.MARKS, count - 10);
                        checkCorrectAns(animLayout, ans, BLinearLayout, ALinearLayout, CLinearLayout, DLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorRed));
                        BLinearLayout.setBackgroundResource(R.drawable.halfcornerred);
                    }
                }
            });
            CLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getOption3().equals(list.get(position).getAnswer())) {
                        initSP();
                        SharePref.write(SharePref.MARKS, (count + 20));
                        checkCorrectAns(animLayout, ans, CLinearLayout, ALinearLayout, BLinearLayout, DLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorGreen));
                        CLinearLayout.setBackgroundResource(R.drawable.halfcirclegreen);

                    } else {
                        initSP();
                        SharePref.write(SharePref.MARKS, count - 10);
                        checkCorrectAns(animLayout, ans, CLinearLayout, ALinearLayout, BLinearLayout, DLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorRed));
                        CLinearLayout.setBackgroundResource(R.drawable.halfcornerred);
                    }
                }
            });
            DLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getOption4().equals(list.get(position).getAnswer())) {
                        initSP();
                        SharePref.write(SharePref.MARKS, (count + 20));
                        checkCorrectAns(animLayout, ans, DLinearLayout, ALinearLayout, BLinearLayout, CLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorGreen));
                        DLinearLayout.setBackgroundResource(R.drawable.halfcirclegreen);

                    } else {
                        initSP();
                        SharePref.write(SharePref.MARKS, count - 10);
                        checkCorrectAns(animLayout, ans, DLinearLayout, ALinearLayout, BLinearLayout, CLinearLayout, list.get(position).getAnswer(), mContext.getResources().getColor(R.color.colorRed));
                        DLinearLayout.setBackgroundResource(R.drawable.halfcornerred);
                    }
                }
            });
            // Return the page
            return itemView;
        }

        void setData(final int cnt) {
            totque.setText(cnt + "/" + list.size());

        }

        void setAnim(final LinearLayout animLayout) {

            animLayout.setVisibility(View.INVISIBLE);

            handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        animation = AnimationUtils.loadAnimation(mContext, R.anim.newanim);
                                        animLayout.startAnimation(animation);
                                    }
                                }
                    , 100);


            animLayout.setVisibility(View.VISIBLE);
        }

        private void initSP() {
            SharePref.init(mContext);
            count = SharePref.read(SharePref.MARKS, 0);
        }

        public void setEnableTrue(LinearLayout opt1, LinearLayout opt2, LinearLayout opt3, LinearLayout opt4, TextView ans) {
            opt1.setEnabled(true);
            opt2.setEnabled(true);
            opt3.setEnabled(true);
            opt4.setEnabled(true);
            opt4.setClickable(true);
            opt4.setClickable(true);
            opt4.setClickable(true);
            opt1.setBackgroundResource(R.drawable.onesidecorner);
            opt2.setBackgroundResource(R.drawable.onesidecorner);
            opt3.setBackgroundResource(R.drawable.onesidecorner);
            opt4.setBackgroundResource(R.drawable.onesidecorner);

            ans.setVisibility(View.INVISIBLE);
        }


        private void checkCorrectAns(final LinearLayout animLayout, final TextView ans, final LinearLayout opt1, final LinearLayout opt2, final LinearLayout opt3, final LinearLayout opt4, String s, final int colors) {


            if ((pager.getCurrentItem() + 1) == list.size()) {
                SharePref.init(mContext);
                final int result = SharePref.read(SharePref.MARKS, 0);
                ans.setVisibility(View.INVISIBLE);
                opt1.setEnabled(false);
                opt2.setEnabled(false);
                opt3.setEnabled(false);
                opt4.setEnabled(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final Dialog dialog = new Dialog(mContext);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.resultdialog);
                        TextView marksText = dialog.findViewById(R.id.marksText);
                        TextView resultStatus = dialog.findViewById(R.id.resultStatus);
                        resultStatus.setText("GAME OVER!");
                        marksText.setText("Your score is:" + result + "/" + list.size()*20);

                        TextView ok = dialog.findViewById(R.id.ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Handler handler1 = new Handler();
                                handler1.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 500);
                            }
                        });
                        dialog.getWindow().getAttributes().windowAnimations = (R.style.DialogAnimation_2);
                        dialog.show();
                        SharePref.clearAll();

                    }
                }, 1000);


            } else {
                ans.setVisibility(View.INVISIBLE);
                opt1.setEnabled(false);
                opt2.setEnabled(false);
                opt3.setEnabled(false);
                opt4.setEnabled(false);


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animLayout.setVisibility(View.GONE);
                        pager.setCurrentItem(pager.getCurrentItem() + 1, false);
                        setEnableTrue(opt1, opt2, opt3, opt4, ans);

                        setAnim(animLayout);
                    }
                }, 1500);

            }
        }


        // Removes the page from the container for the given position.
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}