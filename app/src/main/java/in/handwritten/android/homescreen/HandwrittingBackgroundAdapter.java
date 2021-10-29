package in.handwritten.android.homescreen;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.handwritten.android.splashscreen.R;

class HandwrittingBackgroundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Drawable> drawableList;
    private final Context context;


    public HandwrittingBackgroundAdapter(Context context,List<Drawable> drawableList){
        this.drawableList = drawableList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_step_2_background_paper, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).imageView.setImageDrawable(drawableList.get(position));
    }

    @Override
    public int getItemCount() {
        return drawableList.size();
    }

    public static void  setLocked(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setImageAlpha(128);   // 128 = 0.5
    }

    public static void  setUnlocked(ImageView v) {
        v.setColorFilter(null);
        v.setImageAlpha(255);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageBack);
        }
    }

}
