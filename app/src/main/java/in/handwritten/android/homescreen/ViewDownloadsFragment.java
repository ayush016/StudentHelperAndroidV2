package in.handwritten.android.homescreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import in.handwritten.android.objects.GetAllUserFilesResponse;
import in.handwritten.android.splashscreen.R;
import in.handwritten.android.splashscreen.WebViewActivity;
import in.handwritten.android.utils.SharedPreferenceManager;
import in.handwritten.android.utils.Utils;

import static in.handwritten.android.splashscreen.WebViewActivity.WEB_VIEW_BASE_URL;
import static in.handwritten.android.splashscreen.WebViewActivity.WEB_VIEW_URL;

public class ViewDownloadsFragment extends Fragment implements ViewDownloadsAdapter.ViewDownloadsAdapterInterface {

    private homeScreenPresenter presenter;
    List<GetAllUserFilesResponse.FileList> list;
    LottieAnimationView loadingView;

    public ViewDownloadsFragment(homeScreenPresenter presenter){
        super(R.layout.view_downloads_fragment);
        this.presenter = presenter;
    }

    public ViewDownloadsFragment(){
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = getActivity().findViewById(R.id.loader);
        getActivity().findViewById(R.id.noFilesFound).setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        presenter.getAllUserFiles(SharedPreferenceManager.getUserEmail(getContext()));

    }

    public void inflateFileDownloadAdapter(GetAllUserFilesResponse getAllUserFilesResponse) {
        if(getActivity()==null) return;
        list = getAllUserFilesResponse.getFileList();
        loadingView.setVisibility(View.GONE);
        if (!list.isEmpty()) {
            RecyclerView viewDownloadsView = getActivity().findViewById(R.id.view_downloads_view);
            viewDownloadsView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            viewDownloadsView.setItemAnimator(new DefaultItemAnimator());
            ViewDownloadsAdapter viewDownloadsAdapter =
                    new ViewDownloadsAdapter(list, getAllUserFilesResponse.getFilePath(),getAllUserFilesResponse.getFileInitials());
            viewDownloadsAdapter.setAdapterCallback(this);
            viewDownloadsAdapter.setHasStableIds(true);
            viewDownloadsView.setAdapter(viewDownloadsAdapter);
        } else {
            getActivity().findViewById(R.id.noFilesFound).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPdfImageClicked(String url) {
        if(getActivity()!=null) {
            //Utils.openDefaultBrowser(getActivity(), url);
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra(WEB_VIEW_URL,url);
            intent.putExtra(WEB_VIEW_BASE_URL,"https://docs.google.com/gview?embedded=true&url=");
            startActivity(intent);
        }
    }
}
