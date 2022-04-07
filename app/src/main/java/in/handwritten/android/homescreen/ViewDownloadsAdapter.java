package in.handwritten.android.homescreen;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.handwritten.android.customviews.YoYoAnimatorWrapper;
import in.handwritten.android.objects.GetAllUserFilesResponse;
import in.handwritten.android.splashscreen.R;
import in.handwritten.android.utils.Utils;

class ViewDownloadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<GetAllUserFilesResponse.FileList> drawableList;
    String imagePath;
    String fileInitials;
    ViewDownloadsAdapterInterface viewDownloadsAdapterInterface;

    public ViewDownloadsAdapter(List<GetAllUserFilesResponse.FileList> drawableList, String imagePath, String fileInitials){
        this.drawableList = drawableList;
        this.imagePath = imagePath;
        this.fileInitials = fileInitials;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_step_2_background_paper, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int pos = getItemViewType(position);
        if(!((ViewHolder) holder).isItemAdded) {
            ((ViewHolder) holder).isItemAdded = true;
            if (pos % 2 == 0)
                new YoYoAnimatorWrapper(Techniques.RotateInUpLeft, holder.itemView, 1500).safeCallToYoYo(true);
            else
                new YoYoAnimatorWrapper(Techniques.RotateInUpRight, holder.itemView, 1500).safeCallToYoYo(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ViewHolder) holder).imageView.setClipToOutline(true);
        }
        Picasso.with(((ViewHolder) holder).imageView.getContext()).
                load(imagePath+drawableList.get(pos).getFileName()+".jpg").fit().centerCrop().into(((ViewHolder) holder).imageView, new Callback(){

            @Override
            public void onSuccess() {
                ((ViewHolder) holder).imageViewLoader.setVisibility(View.GONE);
                if(drawableList.get(pos).getFileCount() > 1) {
                    ((ViewHolder) holder).imageMoreFiles.setVisibility(View.VISIBLE);
                    ((ViewHolder) holder).imageMoreFiles.setText(drawableList.get(pos).getFileCount() + " "+ "pages");
                }else {
                    ((ViewHolder) holder).imageMoreFiles.setVisibility(View.VISIBLE);
                    ((ViewHolder) holder).imageMoreFiles.setText(drawableList.get(pos).getFileCount() + " "+ "page");
                }
            }

            @Override
            public void onError() {

            }
        });

        ((ViewHolder) holder).textView.setText(drawableList.get(pos).getFileNameDisplay()+".....");
        ((ViewHolder) holder).textViewCount.setText(Integer.toString(pos+1));

        ((ViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewDownloadsAdapterInterface!=null){
                    viewDownloadsAdapterInterface.onPdfImageClicked(imagePath+drawableList.get(pos).getFileNamePdf()+".pdf");
                }
                //Utils.showWorkInProgressToast(view.getContext(), Integer.toString(position+1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawableList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public ImageView imageViewLoader;
        public TextView textView;
        public TextView textViewCount;
        public TextView imageMoreFiles;
        public boolean isItemAdded;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.imageName);
            textViewCount = itemView.findViewById(R.id.imageCount);
            imageViewLoader = itemView.findViewById(R.id.imageLoader);
            imageMoreFiles = itemView.findViewById(R.id.imageMoreFiles);
        }
    }

    public void setAdapterCallback(ViewDownloadsAdapterInterface viewDownloadsAdapterInterface){
        this.viewDownloadsAdapterInterface = viewDownloadsAdapterInterface;
    }

    public interface ViewDownloadsAdapterInterface{
        void onPdfImageClicked(String url);
    }
}
