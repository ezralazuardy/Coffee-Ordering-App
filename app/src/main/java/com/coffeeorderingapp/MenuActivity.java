package com.coffeeorderingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private String decimalFormat = "%.2f", firstName, lastName;
    private Button btnMenuItem0, btnMenuItem1, btnMenuItem2, btnMenuItem3, btnMenuItem4, btnMenuItem5;
    private ImageButton btnViewMenu, btnBuy;
    private TextView orderFrameTitle, txtPrice;
    private FrameLayout placeholderNoOrder;
    private TableLayout menuItemList;
    private AlertDialog orderDialog, orderItemDetailDialog, cancelOrderDialog;
    private ArrayList<Integer> itemValueList;
    private ArrayList<String> itemTypeList;
    private ArrayList<String> itemNameList;
    private RecyclerView orderRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnMenuItem0 = findViewById(R.id.btn_menuitem_0);
        btnMenuItem1 = findViewById(R.id.btn_menuitem_1);
        btnMenuItem2 = findViewById(R.id.btn_menuitem_2);
        btnMenuItem3 = findViewById(R.id.btn_menuitem_3);
        btnMenuItem4 = findViewById(R.id.btn_menuitem_4);
        btnMenuItem5 = findViewById(R.id.btn_menuitem_5);
        btnViewMenu = findViewById(R.id.btn_view_menu);
        btnBuy = findViewById(R.id.btn_buy);
        orderFrameTitle = findViewById(R.id.textview_title_order);
        txtPrice = findViewById(R.id.textview_price);
        placeholderNoOrder = findViewById(R.id.frame_null_placeholder);
        menuItemList = findViewById(R.id.tablelayout_menu_item_list);
        orderRecyclerView = findViewById(R.id.recyclerview_order);
        initViewAndOrder();
        setRecyclerView();
    }

    private void initViewAndOrder() {
        firstName = getIntent().getStringExtra(getResources().getString(R.string.hint_edittext_firstname_signup));
        lastName = getIntent().getStringExtra(getResources().getString(R.string.hint_edittext_lastname_signup));
        orderFrameTitle.setVisibility(View.GONE);
        orderRecyclerView.setVisibility(View.GONE);
        placeholderNoOrder.setVisibility(View.VISIBLE);
        btnViewMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide_menu));
    }

    private void setRecyclerView() {
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
        itemValueList = new ArrayList<>();
        itemTypeList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        OrderAdapter orderAdapter = new OrderAdapter(itemValueList, itemTypeList, itemNameList);
        orderRecyclerView.setAdapter(orderAdapter);
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

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void incrementPrice(float itemPrice) {
        String currentPrice = txtPrice.getText().toString().replace("$", "").replace(",", ".").trim();
        if (currentPrice.equals(getResources().getString(R.string.bottombar_text_price_nullvalue)))
            txtPrice.setText("$" + String.format(decimalFormat, itemPrice));
        else
            txtPrice.setText("$" + String.format(decimalFormat, Float.valueOf(currentPrice) + itemPrice));
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void decrementPrice(float itemPrice) {
        String currentPrice = txtPrice.getText().toString().replace("$", "").replace(",", ".").trim();
        if (!currentPrice.equals(getResources().getString(R.string.bottombar_text_price_nullvalue))) {
            if (Float.valueOf(currentPrice) - itemPrice > 0)
                txtPrice.setText("$" + String.format(decimalFormat, Float.valueOf(currentPrice) - itemPrice));
            else
                txtPrice.setText(getResources().getString(R.string.bottombar_text_price_nullvalue));
        }
    }

    private void setListener() {
        btnMenuItem0.setOnClickListener(MenuActivity.this);
        btnMenuItem1.setOnClickListener(MenuActivity.this);
        btnMenuItem2.setOnClickListener(MenuActivity.this);
        btnMenuItem3.setOnClickListener(MenuActivity.this);
        btnMenuItem4.setOnClickListener(MenuActivity.this);
        btnMenuItem5.setOnClickListener(MenuActivity.this);
        btnViewMenu.setOnClickListener(MenuActivity.this);
        btnBuy.setOnClickListener(MenuActivity.this);
        cancelOrderDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                cancelOrderDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MenuActivity.this, getResources().getString(R.string.toast_canceled_order), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MenuActivity.this, SignUpActivity.class));
                        finish();
                    }
                });
                cancelOrderDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrderDialog.dismiss();
                    }
                });
            }
        });
    }

    private void unsetListener() {
        if (btnMenuItem0 != null) btnMenuItem0.setOnClickListener(null);
        if (btnMenuItem1 != null) btnMenuItem1.setOnClickListener(null);
        if (btnMenuItem2 != null) btnMenuItem2.setOnClickListener(null);
        if (btnMenuItem3 != null) btnMenuItem3.setOnClickListener(null);
        if (btnMenuItem4 != null) btnMenuItem4.setOnClickListener(null);
        if (btnMenuItem5 != null) btnMenuItem5.setOnClickListener(null);
        if (btnViewMenu != null) btnViewMenu.setOnClickListener(null);
        if (btnBuy != null) btnBuy.setOnClickListener(null);
        if (orderDialog != null) orderDialog.setOnShowListener(null);
        if (cancelOrderDialog != null) cancelOrderDialog.setOnShowListener(null);
    }

    private void buildDialog() {
        cancelOrderDialog = new AlertDialog.Builder(MenuActivity.this)
                .setTitle(getResources().getString(R.string.dialog_title_cancelorder))
                .setMessage(firstName + getResources().getString(R.string.dialog_body_cancelorder))
                .setPositiveButton(getResources().getString(R.string.dialog_btn_yes), null)
                .setNegativeButton(getResources().getString(R.string.dialog_btn_no), null)
                .setCancelable(true)
                .create();
    }

    private void destroyDialog() {
        if (cancelOrderDialog != null) cancelOrderDialog = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menuitem_0:
                showOrderDialog(getResources().getString(R.string.item_name_0));
                break;
            case R.id.btn_menuitem_1:
                showOrderDialog(getResources().getString(R.string.item_name_1));
                break;
            case R.id.btn_menuitem_2:
                showOrderDialog(getResources().getString(R.string.item_name_2));
                break;
            case R.id.btn_menuitem_3:
                showOrderDialog(getResources().getString(R.string.item_name_3));
                break;
            case R.id.btn_menuitem_4:
                showOrderDialog(getResources().getString(R.string.item_name_4));
                break;
            case R.id.btn_menuitem_5:
                showOrderDialog(getResources().getString(R.string.item_name_5));
                break;
            case R.id.btn_view_menu:
                if (menuItemList.getVisibility() == View.VISIBLE) {
                    menuItemList.setVisibility(View.GONE);
                    btnViewMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_show_menu));

                } else if (menuItemList.getVisibility() == View.GONE) {
                    menuItemList.setVisibility(View.VISIBLE);
                    btnViewMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide_menu));
                }
                break;
            case R.id.btn_buy:
                if (!itemValueList.isEmpty() && !itemTypeList.isEmpty() && !itemNameList.isEmpty()) {
                    startActivityForResult(new Intent(MenuActivity.this, ResultActivity.class).putExtra(getResources().getString(R.string.hint_edittext_firstname_signup), firstName).putExtra(getResources().getString(R.string.hint_edittext_lastname_signup), lastName).putIntegerArrayListExtra("itemValueList", itemValueList).putStringArrayListExtra("itemTypeList", itemTypeList).putStringArrayListExtra("itemNameList", itemNameList), 1);
                } else {
                    Toast.makeText(MenuActivity.this, getResources().getString(R.string.toast_null_order), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            startActivity(new Intent(MenuActivity.this, SignUpActivity.class));
            finish();
        }
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void showOrderDialog(final String itemName) {
        final View layoutDialogOrder = getLayoutInflater().inflate(R.layout.dialog_order_item, null);
        TextView txtBody0DialogOrder = layoutDialogOrder.findViewById(R.id.description_order_item_0);
        TextView txtBody1DialogOrder = layoutDialogOrder.findViewById(R.id.description_order_item_1);
        final NumberPicker npDialogOrder = layoutDialogOrder.findViewById(R.id.np_order_item);
        final RadioGroup rbGroupDialogOrder = layoutDialogOrder.findViewById(R.id.rb_group_order_item);
        final AppCompatRadioButton rbType0DialogOrder = layoutDialogOrder.findViewById(R.id.rb_type_0_order_item);
        final AppCompatRadioButton rbType1DialogOrder = layoutDialogOrder.findViewById(R.id.rb_type_1_order_item);
        txtBody0DialogOrder.setText(itemName + " " + getResources().getString(R.string.dialog_body_order_0));
        txtBody1DialogOrder.setText(itemName + " " + getResources().getString(R.string.dialog_body_order_1) + " $" + getPrice(1, itemName) + " " + getResources().getString(R.string.dialog_body_order_2));
        npDialogOrder.setMinValue(Integer.valueOf(getResources().getString(R.string.item_min)));
        npDialogOrder.setMaxValue(Integer.valueOf(getResources().getString(R.string.item_max)));
        rbGroupDialogOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_type_0_order_item:
                        rbType0DialogOrder.setChecked(true);
                        rbType1DialogOrder.setChecked(false);
                        break;
                    case R.id.rb_type_1_order_item:
                        rbType1DialogOrder.setChecked(true);
                        rbType0DialogOrder.setChecked(false);
                        break;
                }
            }
        });
        orderDialog = new AlertDialog.Builder(MenuActivity.this)
                .setTitle(getResources().getString(R.string.dialog_title_order) + " " + itemName)
                .setView(layoutDialogOrder)
                .setPositiveButton(getResources().getString(R.string.dialog_btn_order), null)
                .setNegativeButton(getResources().getString(R.string.dialog_btn_cancel), null)
                .setCancelable(true)
                .create();
        orderDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(MenuActivity.this, getResources().getString(R.string.toast_canceled_order_item), Toast.LENGTH_SHORT).show();
                // dismiss & destroy dialog
                orderDialog.dismiss();
                layoutDialogOrder.destroyDrawingCache();
                rbGroupDialogOrder.setOnCheckedChangeListener(null);
                orderDialog.setOnCancelListener(null);
                orderDialog.setOnShowListener(null);
                orderDialog = null;
            }
        });
        orderDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                orderDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // set placeholder
                        if (itemValueList.isEmpty() && itemTypeList.isEmpty() && itemNameList.isEmpty()) {
                            placeholderNoOrder.setVisibility(View.GONE);
                            orderFrameTitle.setVisibility(View.VISIBLE);
                            orderRecyclerView.setVisibility(View.VISIBLE);
                        }
                        // adding value
                        itemValueList.add(npDialogOrder.getValue());
                        if (rbType0DialogOrder.isChecked())
                            itemTypeList.add(getResources().getString(R.string.item_type_0));
                        else
                            itemTypeList.add(getResources().getString(R.string.item_type_1));
                        itemNameList.add(itemName);
                        incrementPrice(getPrice(npDialogOrder.getValue(), itemName));
                        // adding to recyclerview
                        orderRecyclerView.getAdapter().notifyItemInserted(itemValueList.size() - 1);
                        orderRecyclerView.scrollToPosition(itemValueList.size() - 1);
                        // dismiss & destroy dialog
                        orderDialog.dismiss();
                        layoutDialogOrder.destroyDrawingCache();
                        rbGroupDialogOrder.setOnCheckedChangeListener(null);
                        orderDialog.setOnCancelListener(null);
                        orderDialog.setOnShowListener(null);
                        orderDialog = null;
                    }
                });
                orderDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MenuActivity.this, getResources().getString(R.string.toast_canceled_order_item), Toast.LENGTH_SHORT).show();
                        // dismiss & destroy dialog
                        orderDialog.dismiss();
                        layoutDialogOrder.destroyDrawingCache();
                        rbGroupDialogOrder.setOnCheckedChangeListener(null);
                        orderDialog.setOnCancelListener(null);
                        orderDialog.setOnShowListener(null);
                        orderDialog = null;
                    }
                });
            }
        });
        orderDialog.show();
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void showOrderItemDetailDialog(String itemName, String itemType, int itemValue) {
        final View layoutDialogOrderDetailItem = getLayoutInflater().inflate(R.layout.dialog_order_item_detail, null);
        TextView txtItemName = layoutDialogOrderDetailItem.findViewById(R.id.txt_order_item_detail_name);
        TextView txtItemType = layoutDialogOrderDetailItem.findViewById(R.id.txt_order_item_detail_type);
        TextView txtItemValue = layoutDialogOrderDetailItem.findViewById(R.id.txt_order_item_detail_value);
        TextView txtItemPricePerCup = layoutDialogOrderDetailItem.findViewById(R.id.txt_order_item_detail_pricepercup);
        TextView txtItemTotalItemPrice = layoutDialogOrderDetailItem.findViewById(R.id.txt_order_item_detail_totalprice);
        txtItemName.setText(itemName);
        txtItemType.setText(itemType);
        txtItemValue.setText(String.valueOf(itemValue));
        txtItemPricePerCup.setText("$" + String.valueOf(String.format(decimalFormat, getPrice(1, itemName))).replace(".", ",").trim());
        txtItemTotalItemPrice.setText("$" + String.valueOf(String.format(decimalFormat, getPrice(itemValue, itemName))).replace(".", ",").trim());
        orderItemDetailDialog = new AlertDialog.Builder(MenuActivity.this)
                .setTitle(getResources().getString(R.string.dialog_title_order_item_detail))
                .setView(layoutDialogOrderDetailItem)
                .setPositiveButton(getResources().getString(R.string.dialog_btn_close), null)
                .setCancelable(true)
                .create();
        orderItemDetailDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // destroy dialog
                orderItemDetailDialog.setOnCancelListener(null);
                orderItemDetailDialog.setOnShowListener(null);
                layoutDialogOrderDetailItem.destroyDrawingCache();
                orderItemDetailDialog = null;
            }
        });
        orderItemDetailDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                orderItemDetailDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // dismiss & destroy dialog
                        orderItemDetailDialog.dismiss();
                        orderItemDetailDialog.setOnCancelListener(null);
                        orderItemDetailDialog.setOnShowListener(null);
                        layoutDialogOrderDetailItem.destroyDrawingCache();
                        orderItemDetailDialog = null;
                    }
                });
            }
        });
        orderItemDetailDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cancelorder:
                cancelOrderDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        cancelOrderDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildDialog();
        setListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsetListener();
        destroyDialog();
    }

    // RecyclerView Adapter & ViewHolder
    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

        private List<Integer> itemValueList;
        private List<String> itemTypeList, itemNameList;

        OrderAdapter(List<Integer> itemValueList, List<String> itemTypeList, List<String> itemNameList) {
            this.itemValueList = itemValueList;
            this.itemTypeList = itemTypeList;
            this.itemNameList = itemNameList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_order_layout, parent, false));
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (!itemValueList.isEmpty() && !itemTypeList.isEmpty() && !itemNameList.isEmpty()) {
                    if (itemTypeList.get(position).equals(getResources().getString(R.string.item_type_0))) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_drink_ice));
                    } else if (itemTypeList.get(position).equals(getResources().getString(R.string.item_type_1))) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_drink_hot));
                    }
                holder.itemValue.setText(itemValueList.get(position) + "x");
                holder.itemType.setText(itemTypeList.get(position));
                holder.itemName.setText(itemNameList.get(position));
                holder.itemPrice.setText("$" + String.valueOf(String.format(decimalFormat, getPrice(itemValueList.get(position), itemNameList.get(position)))).replace(".", ",").trim());
            }
        }

        @Override
        public int getItemCount() {
            return itemValueList.size();
        }

        private void deleteItem(int position) {
            decrementPrice(getPrice(itemValueList.get(position), itemNameList.get(position)));
            itemValueList.remove(position);
            itemTypeList.remove(position);
            itemNameList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, itemValueList.size());
            if (itemValueList.isEmpty() && itemTypeList.isEmpty() && itemNameList.isEmpty()) {
                orderFrameTitle.setVisibility(View.GONE);
                orderRecyclerView.setVisibility(View.GONE);
                placeholderNoOrder.setVisibility(View.VISIBLE);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ImageView icon;
            private TextView itemValue, itemType, itemName, itemPrice;
            private ImageButton btnDelete;
            private FrameLayout layout;

            ViewHolder(View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.recyclerview_order_layout);
                icon = itemView.findViewById(R.id.recyclerview_order_icon);
                itemValue = itemView.findViewById(R.id.recyclerview_order_item_value);
                itemType = itemView.findViewById(R.id.recyclerview_order_item_type);
                itemName = itemView.findViewById(R.id.recyclerview_order_item_name);
                itemPrice = itemView.findViewById(R.id.recyclerview_order_item_price);
                btnDelete = itemView.findViewById(R.id.recyclerview_order_btn_delete);
                layout.setOnClickListener(this);
                btnDelete.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.recyclerview_order_layout:
                        showOrderItemDetailDialog(itemNameList.get(getAdapterPosition()), itemTypeList.get(getAdapterPosition()), itemValueList.get(getAdapterPosition()));
                        break;
                    case R.id.recyclerview_order_btn_delete:
                        deleteItem(getAdapterPosition());
                        break;
                }
            }
        }
    }
}
