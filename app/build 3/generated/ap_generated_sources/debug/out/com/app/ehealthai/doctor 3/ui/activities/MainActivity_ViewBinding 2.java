// Generated code from Butter Knife. Do not modify!
package com.app.ehealthai.doctor.ui.activities;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.app.ehealthaidoctor.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.status = Utils.findRequiredViewAsType(source, R.id.status, "field 'status'", TextView.class);
    target.textMessage = Utils.findRequiredViewAsType(source, R.id.textMessage, "field 'textMessage'", TextView.class);
    target.listView = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listView'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.status = null;
    target.textMessage = null;
    target.listView = null;
  }
}
