package muchbeer.raum.com.gcertificationproject1.recyclerv;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import muchbeer.raum.com.gcertificate.data.models.CoinModel;
import muchbeer.raum.com.gcertificationproject1.R;

public class MyCryptoAdapter extends RecyclerView.Adapter<MyCryptoAdapter.CoinViewHolder> {

    List<CoinModel> mItems = new ArrayList<>();
    public final String STR_TEMPLATE_NAME = "%s\t\t\t\t\t\t%s";
    public final String STR_TEMPLATE_PRICE = "%s$\t\t\t\t\t\t24H Volume:\t\t\t%s$";
    private final Handler mHandler = new Handler();

    @NonNull
    @Override
    public MyCryptoAdapter.CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CoinViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCryptoAdapter.CoinViewHolder holder, int position) {
        final CoinModel model = mItems.get(position);
        holder.tvNameAndSymbol.setText(String.format(STR_TEMPLATE_NAME, model.name, model.symbol));
        holder.tvPriceAndVolume.setText(String.format(STR_TEMPLATE_PRICE, model.priceUsd, model.volume24H));
        Glide.with(holder.ivIcon).load(model.imageUrl).into(holder.ivIcon);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    public void setItems(List<CoinModel> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        notifyDataSetChanged();

    }



    public class CoinViewHolder extends RecyclerView.ViewHolder {

        TextView tvNameAndSymbol;
        TextView tvPriceAndVolume;
        ImageView ivIcon;
        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameAndSymbol = itemView.findViewById(R.id.tvNameAndSymbol);
            tvPriceAndVolume = itemView.findViewById(R.id.tvPriceAndVolume);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}
