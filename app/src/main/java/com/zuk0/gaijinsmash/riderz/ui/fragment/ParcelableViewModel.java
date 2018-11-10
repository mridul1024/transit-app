package com.zuk0.gaijinsmash.riderz.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

public interface ParcelableViewModel {
    void writeTo(@NonNull Bundle bundle);
    void readFrom(@NonNull Bundle bundle);
}
