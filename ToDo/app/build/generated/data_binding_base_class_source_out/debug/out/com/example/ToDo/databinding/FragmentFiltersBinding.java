// Generated by view binder compiler. Do not edit!
package com.example.ToDo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.ToDo.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentFiltersBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final LinearLayout filtersRoot;

  @NonNull
  public final ListView listFiltered;

  @NonNull
  public final Spinner spinnerFilter;

  private FragmentFiltersBinding(@NonNull LinearLayout rootView, @NonNull LinearLayout filtersRoot,
      @NonNull ListView listFiltered, @NonNull Spinner spinnerFilter) {
    this.rootView = rootView;
    this.filtersRoot = filtersRoot;
    this.listFiltered = listFiltered;
    this.spinnerFilter = spinnerFilter;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentFiltersBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentFiltersBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_filters, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentFiltersBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      LinearLayout filtersRoot = (LinearLayout) rootView;

      id = R.id.listFiltered;
      ListView listFiltered = ViewBindings.findChildViewById(rootView, id);
      if (listFiltered == null) {
        break missingId;
      }

      id = R.id.spinnerFilter;
      Spinner spinnerFilter = ViewBindings.findChildViewById(rootView, id);
      if (spinnerFilter == null) {
        break missingId;
      }

      return new FragmentFiltersBinding((LinearLayout) rootView, filtersRoot, listFiltered,
          spinnerFilter);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
