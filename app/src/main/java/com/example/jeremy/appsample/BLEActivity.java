package com.example.jeremy.appsample;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class BLEActivity extends AppCompatActivity {

    public final static String TAG = "BLEActivity";

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    ListView mListView;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_devices);
        }
        else {
            Log.w(TAG, "ActionBar is NULL");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        mListView = (ListView) findViewById(R.id.bleListView);
        mListView.setOnItemClickListener(mOnItemClickListener);
        setupActionBar();
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        registerReceiver(mReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        mListView.setAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    AdapterView.OnItemClickListener mOnItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view,
                                        int position, long id) {
                    Log.d(TAG, "item clicked");
                    final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                    if (device == null) return;
                    final Intent intent = new Intent(BLEActivity.this, DeviceControlActivity.class);
                    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
                    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                    if (mScanning) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mScanning = false;
                    }
                    startActivity(intent);
                }
            };

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            mBluetoothAdapter.startDiscovery();
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    protected static HashMap<BluetoothDevice, Integer> mRssiMap = new
                        HashMap<BluetoothDevice, Integer>();

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;

        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = BLEActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public int getIndex(BluetoothDevice device) {
            return mLeDevices.indexOf(device);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.deviceType = (TextView) view.findViewById(R.id.device_type);
                viewHolder.deviceRssi = (TextView) view.findViewById(R.id.device_rssi);
                viewHolder.deviceBondState = (TextView) view.findViewById(R.id.device_bond_state);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);

            switch (device.getType()) {
                case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                    viewHolder.deviceType.setText(R.string.device_type_classic);
                    break;
                case BluetoothDevice.DEVICE_TYPE_DUAL:
                    viewHolder.deviceType.setText(R.string.device_type_dual);
                    break;
                case BluetoothDevice.DEVICE_TYPE_LE:
                    viewHolder.deviceType.setText(R.string.device_type_le);
                    break;
                case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                    viewHolder.deviceType.setText(R.string.device_type_unknown);
                    break;
            }
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_NONE:
                    viewHolder.deviceBondState.setText(R.string.device_bond_state_none);
                    break;
                case BluetoothDevice.BOND_BONDING:
                    viewHolder.deviceBondState.setText(R.string.device_bond_state_bonding);
                    break;
                case BluetoothDevice.BOND_BONDED:
                    viewHolder.deviceBondState.setText(R.string.device_bond_state_bonded);
                    break;
            }
            viewHolder.deviceAddress.setText(device.getAddress());
            Integer rssi = mRssiMap.get(device);
            if (rssi != null && viewHolder.deviceRssi != null)
                viewHolder.deviceRssi.setText(rssi.toString() + " dBm");

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private void updateRssi(BluetoothDevice device, int rssi) {
        int index = mLeDeviceListAdapter.getIndex(device);
        Log.d(TAG, "update rssi at index: " + index);
        mRssiMap.put(device, rssi);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String mac = device.getAddress();
            int type = device.getType();
            Log.d(TAG, "name: " + name + "  rssi: " + rssi + "  mac: " + mac +
                        "  type: " + type);
            updateRssi(device, rssi);
            /*
            String device = intent.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
            String uuid = intent.getStringExtra(BluetoothDevice.EXTRA_UUID);
            String bond_state = intent.getStringExtra(BluetoothDevice.EXTRA_BOND_STATE);
            Log.d(TAG, "name: " + name + "  rssi: " + rssi +"  device: " +
                device + "  uuid: " + uuid + "  bond_state:" + bond_state);
            */
            //Toast.makeText(getApplicationContext(),"  RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();
        }
    }
};


    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceType;
        TextView deviceRssi;
        TextView deviceBondState;
    }

}
