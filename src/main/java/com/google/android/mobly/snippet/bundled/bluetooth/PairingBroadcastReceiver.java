/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.android.mobly.snippet.bundled.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.mobly.snippet.bundled.utils.Utils;
import com.google.android.mobly.snippet.util.Log;

/** Broadcast receiver for auto-confirming pairing requests. */
public class PairingBroadcastReceiver extends BroadcastReceiver {
    private final Context mContext;
    public static final IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);

    public PairingBroadcastReceiver(Context context) throws Throwable {
        mContext = context;
        Utils.adaptShellPermissionIfRequired(mContext);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device != null) {
                Log.d("Confirming pairing with device: " + device.getAddress());
                device.setPairingConfirmation(true);
            }
            try {
                mContext.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                Log.w("Receiver not registered", e);
            }
        }
    }
}
