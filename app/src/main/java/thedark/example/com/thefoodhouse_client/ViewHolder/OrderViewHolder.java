package thedark.example.com.thefoodhouse_client.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import thedark.example.com.thefoodhouse_client.Interface.ItemClickListener;
import thedark.example.com.thefoodhouse_client.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress, txtOrderTotal;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);
        txtOrderAddress = (TextView) itemView.findViewById(R.id.order_address);
        txtOrderTotal = (TextView) itemView.findViewById(R.id.order_total);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
