package com.example.freshdiet;

import android.os.Bundle;

// 프래그먼트 전환 인터페이스
public interface FragmentCallback {
    void onChangedFragment(int position, Bundle bundle);
}
