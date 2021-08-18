package com.example.cs_360fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WeightTrackingActivity extends AppCompatActivity
        implements EditWeightDialogFragment.OnEditWeightListener,
                    AddWeightDialogFragment.OnAddWeightListener {
    private String mUsername = null;

    private FitnessAppDatabase mDatabase = null;
    private RecyclerView mRecyclerView = null;
    private WeightAdapter mWeightAdapter = null;

    private Weight mSelectedWeight = null;
    private int mSelectedWeightPosition = RecyclerView.NO_POSITION;
    private ActionMode mActionMode = null;

    private float mGoalWeight = 0.f;

    private List<Weight> getWeights() {
        return mDatabase.weightDao().getWeights(mUsername);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracking);

        if(savedInstanceState != null) {
            mUsername = savedInstanceState.getString("username");
            mSelectedWeightPosition = savedInstanceState.getInt("selectedWeightPosition");
        }

        Intent intent = getIntent();
        mUsername = intent.getStringExtra("username");

        mDatabase = FitnessAppDatabase.getInstance(getApplicationContext());

        mRecyclerView = findViewById(R.id.listRecyclerView);

        RecyclerView.LayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mWeightAdapter = new WeightAdapter(getWeights());
        mRecyclerView.setAdapter(mWeightAdapter);

        mGoalWeight = mDatabase.accountDao().getGoalWeight(mUsername);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("username", mUsername);
        savedInstanceState.putInt("selectedWeightPosition", mSelectedWeightPosition);
    }

    public void onButtonAddWeightClick(View view){
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }

        FragmentManager manager = getSupportFragmentManager();
        AddWeightDialogFragment dialog = new AddWeightDialogFragment();

        dialog.show(manager, "addWeightDialog");
    }

    @Override
    public void onAddWeight(float newWeight) {
        String sendSMSPermission = Manifest.permission.SEND_SMS;

        Weight weight = new Weight(newWeight, mUsername);

        mDatabase.weightDao().insertWeight(weight);

        mWeightAdapter.addWeight(weight);

        if(weight != null &&
                weight.getWeight() <= mGoalWeight &&
                checkSelfPermission(sendSMSPermission) == PackageManager.PERMISSION_GRANTED){
            String phoneNumber = getString(R.string.default_phone_number);
            String message = String.format("You have reached your goal weight of %f Lbs!", mGoalWeight);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,null, message, null, null);
        }
    }

    @Override
    public void onEditWeight(float newWeight) {
        if(mSelectedWeightPosition == RecyclerView.NO_POSITION) {
            throw new RuntimeException();
        } else if(mSelectedWeight == null) {
            mSelectedWeight = getWeights().get(mSelectedWeightPosition);
        }

        mSelectedWeight.setWeight(newWeight);

        mDatabase.weightDao().updateWeight(mSelectedWeight);

        mWeightAdapter.editWeight(mSelectedWeightPosition, mSelectedWeight);

        mSelectedWeight = null;
        mSelectedWeightPosition = RecyclerView.NO_POSITION;
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        private boolean dialogCreated = false;

        public void deselectItem(){
            int oldPos = mSelectedWeightPosition;
            mSelectedWeight = null;
            mSelectedWeightPosition = RecyclerView.NO_POSITION;
            mWeightAdapter.notifyItemChanged(oldPos);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.actionModeDelete:
                    mDatabase.weightDao().deleteWeight(mSelectedWeight);
                    mWeightAdapter.removeWeight(mSelectedWeightPosition);

                    actionMode.finish();
                    return true;

                case R.id.actionModeEdit:
                    FragmentManager manager = getSupportFragmentManager();
                    EditWeightDialogFragment dialog = new EditWeightDialogFragment();

                    dialog.show(manager, "editWeightDialog");
                    dialogCreated = true;

                    actionMode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            if(!dialogCreated){
                deselectItem();
            }// do not prematurely deselect an item if an edit dialog was opened

            mActionMode = null;
        }
    };

    private class WeightHolder extends RecyclerView.ViewHolder
                            implements View.OnClickListener {
        TextView mTextViewWeightDate = null;
        TextView mTextViewWeightValue = null;

        Weight mWeight = null;

        public WeightHolder(LayoutInflater layoutInflater, ViewGroup parent){
            super(layoutInflater.inflate(R.layout.recycler_view_items,parent, false));

            itemView.setOnClickListener(this);

            mTextViewWeightDate = itemView.findViewById(R.id.textViewWeightDate);
            mTextViewWeightValue = itemView.findViewById(R.id.textViewWeightValue);
        }

        public void bind(Weight weight, int position) {
            mWeight = weight;

            mTextViewWeightDate.setText(String.format("%tD", mWeight.getDateTime()));
            mTextViewWeightValue.setText(String.format("%f Lbs", mWeight.getWeight()));

            if(mSelectedWeightPosition == position) {
                mTextViewWeightValue.getRootView().setBackgroundColor(getResources().getColor(R.color.teal_200, getTheme()));
            } else {
                mTextViewWeightValue.getRootView().setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        public void onClick(View view) {
            if (mActionMode != null) {
                mActionMode.finish();
                mActionMode = null;
            }

            mSelectedWeight = mWeight;
            mSelectedWeightPosition = getAdapterPosition();

            mWeightAdapter.notifyItemChanged(mSelectedWeightPosition);

            mActionMode = WeightTrackingActivity.this.startActionMode(mActionModeCallback);
        }
    }

    private class WeightAdapter extends RecyclerView.Adapter<WeightHolder> {
        private List<Weight> mWeights;

        public WeightAdapter(List<Weight> weights) {
            mWeights = weights;
        }

        public void addWeight(Weight weight) {
            mWeights.add(0, weight);

            notifyItemInserted(0);

            mRecyclerView.scrollToPosition(0);
        }

        public void removeWeight(int position){
            mWeights.remove(position);

            notifyItemRemoved(position);
        }

        public void editWeight(int position, Weight newWeight) {
            mWeights.set(position, newWeight);

            notifyItemChanged(position);
        }

        @NonNull
        @Override
        public WeightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new WeightHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull WeightHolder holder, int position) {
            holder.bind(mWeights.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mWeights.size();
        }
    }
}