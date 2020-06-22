package nitish.build.com.freemium.Fragments;

//                           ____        _   _ _ _   _     _
//     /\                   |  _ \      | \ | (_) | (_)   | |
//    /  \   _ __  _ __  ___| |_) |_   _|  \| |_| |_ _ ___| |__
//   / /\ \ | '_ \| '_ \/ __|  _ <| | | | . ` | | __| / __| '_ \
//  / ____ \| |_) | |_) \__ \ |_) | |_| | |\  | | |_| \__ \ | | |
// /_/    \_\ .__/| .__/|___/____/ \__, |_| \_|_|\__|_|___/_| |_|
//          | |   | |               __/ |
//          |_|   |_|              |___/
//
//                 Freemium Music
//   Developed and Maintained by Nitish Gadangi

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
