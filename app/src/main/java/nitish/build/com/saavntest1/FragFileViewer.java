package nitish.build.com.saavntest1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codekidlabs.storagechooser.StorageChooser;

public class FragFileViewer extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        StorageChooser chooser = new StorageChooser.Builder()
                .withActivity(getActivity())
                .withFragmentManager(getActivity().getFragmentManager())
                .disableMultiSelect()
                .allowCustomPath(true)
                .setType(StorageChooser.FILE_PICKER)
                .build();
        chooser.show();

        return super.onCreateView(inflater, container, savedInstanceState);


    }
}
