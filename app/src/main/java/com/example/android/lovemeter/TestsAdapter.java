package com.example.android.lovemeter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noureldeen on 1/20/2018.
 */

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.TestsViewHolder> {
    private final Context context;
    private final List<TestModel> testModels;
    private final TestsClick testsClick;

    public interface TestsClick {
        void OnClick(int position, String testerName, String testNumber);
    }

    public TestsAdapter(Context context, List<TestModel> userModels, TestsClick testsClick) {
        this.context = context;
        this.testModels = userModels;
        this.testsClick = testsClick;
    }

    @Override
    public TestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final TestsViewHolder holder, int position) {
        if (holder != null) {
            holder.bindTest(testModels.get(holder.getAdapterPosition()).getTesterName(), testModels.get(holder.getAdapterPosition()).getTestNumber());
            holder.testerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    testsClick.OnClick(holder.getAdapterPosition(), testModels.get(holder.getAdapterPosition()).getTesterName(), testModels.get(holder.getAdapterPosition()).getTestNumber());
                }
            });
            holder.testName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    testsClick.OnClick(holder.getAdapterPosition(), testModels.get(holder.getAdapterPosition()).getTesterName(), testModels.get(holder.getAdapterPosition()).getTestNumber());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return testModels.size();
    }

    class TestsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_testerName)
        TextView testerName;
        @BindView(R.id.tv_testName)
        TextView testName;

        TestsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindTest(String tsterName, String tstName) {
            testerName.setText(tsterName);
            testName.setText(tstName);
        }
    }
}
