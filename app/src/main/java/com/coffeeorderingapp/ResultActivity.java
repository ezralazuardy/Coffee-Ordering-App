package com.coffeeorderingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {

    private String decimalFormat = "%.2f";
    private ArrayList<Integer> itemValueList;
    private ArrayList<String> itemTypeList, itemNameList;
    private LinearLayout layout0, layout1, layout2;
    private TextView title, body, totalItem, totalPrice, orderFor, taxInfo;
    private RecyclerView orderedItemRecyclerView;
    private Button btnConfirm, btnCancel;
    private AlertDialog orderedItemDetailDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        layout0 = findViewById(R.id.result_layout_0);
        layout1 = findViewById(R.id.result_layout_1);
        layout2 = findViewById(R.id.result_layout_2);
        title = findViewById(R.id.result_text_title);
        body = findViewById(R.id.result_text_body);
        totalItem = findViewById(R.id.result_text_totalitem);
        orderFor = findViewById(R.id.result_text_orderfor);
        totalPrice = findViewById(R.id.result_text_totalprice);
        taxInfo = findViewById(R.id.result_text_taxtinfo);
        orderedItemRecyclerView = findViewById(R.id.recyclerview_order_result);
        btnConfirm = findViewById(R.id.btn_result_confirm);
        btnCancel = findViewById(R.id.btn_result_cancel);
        initViewAndOrder();
        setRecyclerView();
    }

    @SuppressLint("SetTextI18n")
    private void initViewAndOrder() {
        String firstName = getIntent().getStringExtra(getResources().getString(R.string.hint_edittext_firstname_signup));
        String lastName = getIntent().getStringExtra(getResources().getString(R.string.hint_edittext_lastname_signup));
        itemValueList = getIntent().getIntegerArrayListExtra("itemValueList");
        itemTypeList = getIntent().getStringArrayListExtra("itemTypeList");
        itemNameList = getIntent().getStringArrayListExtra("itemNameList");
        // error handling. force finishing activity when get an anomaly of null imported variable
        if (firstName.isEmpty() || lastName.isEmpty() || itemValueList.isEmpty() || itemTypeList.isEmpty() || itemNameList.isEmpty()) finish();
        layout0.setVisibility(View.VISIBLE);
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        body.setText(firstName + getResources().getString(R.string.result_text_body));
        totalItem.setText(" " + String.valueOf(getTotalItem()));
        orderFor.setText(firstName + " " + lastName);
        totalPrice.setText("$" + String.valueOf(String.format(decimalFormat, getTotalPrice()).replace(".", ",").trim()));
        taxInfo.setText(getResources().getString(R.string.result_text_taxinfo_0) + " " + getResources().getString(R.string.tax_amount) + getResources().getString(R.string.result_text_taxinfo_1));
    }

    private float getPrice(int value, String itemName) {
        if (itemName.equals(getResources().getString(R.string.item_name_0))) {
            return value * Float.valueOf(getResources().getString(R.string.item_price_0));
        } else if (itemName.equals(getResources().getString(R.string.item_name_1))) {
            return value * Float.valueOf(getResources().getString(R.string.item_price_1));
        } else if (itemName.equals(getResources().getString(R.string.item_name_2))) {
            return value * Float.valueOf(getResources().getString(R.string.item_price_2));
        } else if (itemName.equals(getResources().getString(R.string.item_name_3))) {
            return value * Float.valueOf(getResources().getString(R.string.item_price_3));
        } else if (itemName.equals(getResources().getString(R.string.item_name_4))) {
            return value * Float.valueOf(getResources().getString(R.string.item_price_4));
        } else if (itemName.equals(getResources().getString(R.string.item_name_5))) {
            return value * Float.valueOf(getResources().getString(R.string.item_price_5));
        } else {
            return 0;
        }
    }

    private int getTotalItem() {
        int totalItem = 0;
        for (int i : itemValueList) totalItem += i;
        return totalItem;
    }

    private float getTotalPrice() {
        float totalPrice = 0;
        for (int i = 0; i < itemValueList.size(); i++) totalPrice += (getPrice(itemValueList.get(i), itemNameList.get(i)));
        return totalPrice + getTaxAmount(totalPrice);
    }

    private float getTaxAmount(float totalPrice) {
        return totalPrice * (float) (Integer.valueOf(getResources().getString(R.string.tax_amount)) / 100.0);
    }

    private void setRecyclerView() {
        orderedItemRecyclerView.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
        OrderedItemAdapter orderedItemAdapter = new OrderedItemAdapter(itemValueList, itemTypeList, itemNameList);
        orderedItemRecyclerView.setAdapter(orderedItemAdapter);
    }

    private void setListener() {
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void unsetListener() {
        btnConfirm.setOnClickListener(null);
        btnCancel.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_result_confirm:
                switch (getActiveLayout()) {
                    case 0:
                        layout0.setVisibility(View.GONE);
                        layout1.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        btnCancel.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_back_red), null, null, null);
                        btnCancel.setText(getResources().getString(R.string.dialog_btn_back));
                        break;
                    case 1:
                        layout0.setVisibility(View.GONE);
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.GONE);
                        btnConfirm.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_close_green), null, null, null);
                        btnConfirm.setText(getResources().getString(R.string.dialog_btn_close));
                        title.setText(getResources().getString(R.string.result_text_title_confirmedorder));
                        break;
                    case 2:
                        setResult(Activity.RESULT_OK, new Intent());
                        finish();
                        break;
                }
                break;
            case R.id.btn_result_cancel:
                switch (getActiveLayout()) {
                    case 0:
                        setResult(Activity.RESULT_CANCELED, new Intent());
                        finish();
                        break;
                    case 1:
                        layout0.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        btnCancel.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_close_red), null, null, null);
                        btnCancel.setText(getResources().getString(R.string.dialog_btn_cancel));
                        break;
                }
                break;
        }
    }

    private int getActiveLayout() {
        if (layout0.getVisibility() == View.VISIBLE && layout1.getVisibility() == View.GONE && layout2.getVisibility() == View.GONE) {
            return 0;
        } else if (layout0.getVisibility() == View.GONE && layout1.getVisibility() == View.VISIBLE && layout2.getVisibility() == View.GONE) {
            return 1;
        } else if (layout0.getVisibility() == View.GONE && layout1.getVisibility() == View.GONE && layout2.getVisibility() == View.VISIBLE) {
            return 2;
        } else {
            return -1;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, new Intent());
        finish();
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void showOrderedItemDetailDialog(String itemName, String itemType, int itemValue) {
        final View layoutDialogOrderedDetailItem = getLayoutInflater().inflate(R.layout.dialog_order_item_detail, null);
        TextView txtItemName = layoutDialogOrderedDetailItem.findViewById(R.id.txt_order_item_detail_name);
        TextView txtItemType = layoutDialogOrderedDetailItem.findViewById(R.id.txt_order_item_detail_type);
        TextView txtItemValue = layoutDialogOrderedDetailItem.findViewById(R.id.txt_order_item_detail_value);
        TextView txtItemPricePerCup = layoutDialogOrderedDetailItem.findViewById(R.id.txt_order_item_detail_pricepercup);
        TextView txtItemTotalItemPrice = layoutDialogOrderedDetailItem.findViewById(R.id.txt_order_item_detail_totalprice);
        txtItemName.setText(itemName);
        txtItemType.setText(itemType);
        txtItemValue.setText(String.valueOf(itemValue));
        txtItemPricePerCup.setText("$" + String.valueOf(String.format(decimalFormat, getPrice(1, itemName))).replace(".", ",").trim());
        txtItemTotalItemPrice.setText("$" + String.valueOf(String.format(decimalFormat, getPrice(itemValue, itemName))).replace(".", ",").trim());
        orderedItemDetailDialog = new android.support.v7.app.AlertDialog.Builder(ResultActivity.this)
                .setTitle(getResources().getString(R.string.dialog_title_order_item_detail))
                .setView(layoutDialogOrderedDetailItem)
                .setPositiveButton(getResources().getString(R.string.dialog_btn_close), null)
                .setCancelable(true)
                .create();
        orderedItemDetailDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // destroy dialog
                orderedItemDetailDialog.setOnCancelListener(null);
                orderedItemDetailDialog.setOnShowListener(null);
                layoutDialogOrderedDetailItem.destroyDrawingCache();
                orderedItemDetailDialog = null;
            }
        });
        orderedItemDetailDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                orderedItemDetailDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // dismiss & destroy dialog
                        orderedItemDetailDialog.dismiss();
                        orderedItemDetailDialog.setOnCancelListener(null);
                        orderedItemDetailDialog.setOnShowListener(null);
                        layoutDialogOrderedDetailItem.destroyDrawingCache();
                        orderedItemDetailDialog = null;
                    }
                });
            }
        });
        orderedItemDetailDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsetListener();
    }

    // RecyclerView Adapter & ViewHolder
    private class OrderedItemAdapter extends RecyclerView.Adapter<OrderedItemAdapter.ViewHolder> {

        private List<Integer> itemValueList;
        private List<String> itemTypeList, itemNameList;

        OrderedItemAdapter(List<Integer> itemValueList, List<String> itemTypeList, List<String> itemNameList) {
            this.itemValueList = itemValueList;
            this.itemTypeList = itemTypeList;
            this.itemNameList = itemNameList;
        }

        @NonNull
        @Override
        public OrderedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OrderedItemAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_ordereditem_layout, parent, false));
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onBindViewHolder(@NonNull OrderedItemAdapter.ViewHolder holder, int position) {
            if (!itemValueList.isEmpty() && !itemTypeList.isEmpty() && !itemNameList.isEmpty()) {
                if (itemTypeList.get(position).equals(getResources().getString(R.string.item_type_0))) {
                    holder.icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_drink_ice));
                } else if (itemTypeList.get(position).equals(getResources().getString(R.string.item_type_1))) {
                    holder.icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_drink_hot));
                }
                holder.itemValue.setText(itemValueList.get(position) + "x");
                holder.itemType.setText(itemTypeList.get(position));
                holder.itemName.setText(itemNameList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return itemValueList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView icon;
            private TextView itemValue, itemType, itemName;
            private LinearLayout layout;

            ViewHolder(View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.recyclerview_ordereditem_layout);
                icon = itemView.findViewById(R.id.recyclerview_ordereditem_icon);
                itemValue = itemView.findViewById(R.id.recyclerview_ordereditem_value);
                itemType = itemView.findViewById(R.id.recyclerview_ordereditem_type);
                itemName = itemView.findViewById(R.id.recyclerview_ordereditem_name);
                layout.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.recyclerview_ordereditem_layout:
                        showOrderedItemDetailDialog(itemNameList.get(getAdapterPosition()), itemTypeList.get(getAdapterPosition()), itemValueList.get(getAdapterPosition()));
                        break;
                }
            }
        }
    }
}
